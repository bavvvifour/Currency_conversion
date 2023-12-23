package servlet;

import com.google.gson.Gson;
import dao.*;
import model.exchangeRatesModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(urlPatterns = "/exchange")
public class exchange extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(123);
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        double amountToConvertParam = Double.parseDouble(req.getParameter("amount"));

        exchangeRatesModel convertCurrency = exchangeDAO.convertCurrency(baseCurrencyCode, targetCurrencyCode,
                amountToConvertParam);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String json = gson.toJson(convertCurrency);
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }
}
