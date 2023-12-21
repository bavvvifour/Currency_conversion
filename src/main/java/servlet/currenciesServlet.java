package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Currency;
import java.util.List;

import com.google.gson.Gson;
import dao.currenciesDAO;
import model.currenciesModel;


@WebServlet(urlPatterns = "/currencies")
public class currenciesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<currenciesModel> currencies = currenciesDAO.getAllCurrencies();
        Gson gson = new Gson();
        String json = gson.toJson(currencies);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String Code = req.getParameter("Code");
        String FullName = req.getParameter("FullName");
        String Sign = req.getParameter("Sign");

        currenciesDAO.addCurrency(Code, FullName, Sign);

        List<currenciesModel> updatedCurrencies = currenciesDAO.getAllCurrencies();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String json = gson.toJson(updatedCurrencies);
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }
}
