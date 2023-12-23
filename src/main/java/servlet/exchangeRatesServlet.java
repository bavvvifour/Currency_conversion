package servlet;

import com.google.gson.Gson;
import dao.exchangeRatesDAO;
import model.exchangeRatesModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(urlPatterns = "/exchangeRates")
public class exchangeRatesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<exchangeRatesModel> exchangeRates = exchangeRatesDAO.getAllExchangeRates();

        Gson gson = new Gson();
        String json = gson.toJson(exchangeRates);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        double rate = Double.parseDouble(req.getParameter("rate"));
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");

        exchangeRatesDAO.addExchangeRate(baseCurrencyCode, targetCurrencyCode, rate);

        List<exchangeRatesModel> updatedExchangeRates = exchangeRatesDAO.getAllExchangeRates();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String json = gson.toJson(updatedExchangeRates);
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }
}
