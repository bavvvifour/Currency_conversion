package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.ExchangeRatesDAO;
import exception.CurrencyErrorResponse;
import exception.ExchangeRatesErrorResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet(urlPatterns = "/exchange")
public class ExchangeServlet extends HttpServlet {
    private final ExchangeRatesDAO exchangeRatesDAO = new ExchangeRatesDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("from").toUpperCase();
        String targetCurrencyCode = req.getParameter("to").toUpperCase();
        String StringAmount = req.getParameter("amount");

        if (baseCurrencyCode.isBlank()) {
            resp.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExchangeRatesErrorResponse(
                    SC_BAD_REQUEST,
                    "Missing parameter - from"
            ));
        }
        if (targetCurrencyCode.isBlank()) {
            resp.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExchangeRatesErrorResponse(
                    SC_BAD_REQUEST,
                    "Missing parameter - to"
            ));
        }
        if (StringAmount == null || StringAmount.isBlank()) {
            resp.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExchangeRatesErrorResponse(
                    SC_BAD_REQUEST,
                    "Missing parameter - amount"
            ));
        }
        if (baseCurrencyCode.length() != 3 || targetCurrencyCode.length() != 3) {
            resp.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExchangeRatesErrorResponse(
                    SC_BAD_REQUEST,
                    "Target currency or Base currency code must be in ISO 4217 format"
            ));
        }

        BigDecimal amount = null;
        try {
            assert StringAmount != null;
            amount = BigDecimal.valueOf(Double.parseDouble(StringAmount));
        } catch (NumberFormatException e) {
            resp.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExchangeRatesErrorResponse(
                    SC_BAD_REQUEST,
                    "Incorrect value of amount parameter"
            ));
        }

        try {
            if (exchangeRatesDAO.getByCode(baseCurrencyCode+targetCurrencyCode) != null) {
                objectMapper.writeValue(resp.getWriter(),
                        exchangeRatesDAO.convertCurrency(baseCurrencyCode, targetCurrencyCode, amount
                        ));
            } else {
                resp.setStatus(SC_NOT_FOUND);
                objectMapper.writeValue(resp.getWriter(), new ExchangeRatesErrorResponse(
                        SC_NOT_FOUND,
                        "Что-то пошло не так :("
                ));
            }
        } catch (SQLException e) {
            resp.setStatus(SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new CurrencyErrorResponse(
                    SC_INTERNAL_SERVER_ERROR,
                    "Something happened with the database, try again later!"
            ));
        }
    }
}
