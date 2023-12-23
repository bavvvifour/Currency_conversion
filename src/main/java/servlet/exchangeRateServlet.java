package servlet;

import com.google.gson.Gson;
import dao.exchangeRateDAO;
import model.exchangeRatesModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/exchangeRate/*")
public class exchangeRateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // ПУСТЬ ПЕРВЫВЙ КОД БУДЕТ ОБЯЗАТЕЛЬНО СОДЕРЖАТЬ 3 СИМВОЛА
        String nameCurrency = req.getPathInfo();
        if (nameCurrency != null) {
            String[] pathParts = nameCurrency.split("/");
            if (pathParts.length > 1) {
                String name = pathParts[1];

                String first_part = name.substring(0,3);
                String second_part = name.substring(3);

                exchangeRatesModel exchangeRate = exchangeRateDAO.getexchangeRate(first_part, second_part);

                Gson gson = new Gson();
                String json = gson.toJson(exchangeRate);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                PrintWriter out = resp.getWriter();
                out.print(json);
                out.flush();
            }
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equals("PATCH")) {
            super.service(req, resp);
        }

        this.doPatch(req, resp);
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        double rate = Double.parseDouble(req.getParameter("rate"));
        // ПУСТЬ ПЕРВЫВЙ КОД БУДЕТ ОБЯЗАТЕЛЬНО СОДЕРЖАТЬ 3 СИМВОЛА
        String nameCurrency = req.getPathInfo();
        if (nameCurrency != null) {
            String[] pathParts = nameCurrency.split("/");
            if (pathParts.length > 1) {
                String name = pathParts[1];

                String first_part = name.substring(0,3);
                String second_part = name.substring(3);

                exchangeRateDAO.updateExchangeRate(first_part,
                        second_part, rate);

                exchangeRatesModel exchangeRate = exchangeRateDAO.getexchangeRate(first_part, second_part);

                Gson gson = new Gson();
                String json = gson.toJson(exchangeRate);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                PrintWriter out = resp.getWriter();
                out.print(json);
                out.flush();
            }
        }
    }
}
