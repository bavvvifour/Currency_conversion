package servlet;

import com.google.gson.Gson;
import dao.currencyDAO;
import model.currenciesModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/currency/*")
public class currencyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nameCurrency = req.getPathInfo();
        if (nameCurrency != null) {
            String[] pathParts = nameCurrency.split("/");
            if (pathParts.length > 1) {
                String name = pathParts[1];
                currenciesModel currencies = currencyDAO.getCurrency(name);

                Gson gson = new Gson();
                String json = gson.toJson(currencies);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                PrintWriter out = resp.getWriter();
                out.print(json);
                out.flush();
            }
        }
    }
}
