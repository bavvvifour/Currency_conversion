package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CurrenciesDAO;
import dao.ExchangeRatesDAO;
import exception.CurrencyErrorResponse;
import exception.ExchangeRatesErrorResponse;
import exception.StringToBigDecimalErrorResponse;
import model.ExchangeRatesModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet(urlPatterns = "/exchangeRates")
public class exchangeRatesServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExchangeRatesDAO exchangeRatesDAO = new ExchangeRatesDAO();
    private final CurrenciesDAO currenciesDAO = new CurrenciesDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            objectMapper.writeValue(resp.getWriter(), exchangeRatesDAO.getAll());
        } catch (SQLException e) {
            resp.setStatus(SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ExchangeRatesErrorResponse(
                    SC_INTERNAL_SERVER_ERROR,
                    "Something happened with the database, try again later!"
            ));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode").toUpperCase();
        String targetCurrencyCode = req.getParameter("targetCurrencyCode").toUpperCase();
        String StringRate = req.getParameter("rate");

        if (baseCurrencyCode.isBlank()) {
            resp.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExchangeRatesErrorResponse(
                    SC_BAD_REQUEST,
                    "Missing parameter - baseCurrencyCode"
            ));
        }
        if (targetCurrencyCode.isBlank()) {
            resp.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExchangeRatesErrorResponse(
                    SC_BAD_REQUEST,
                    "Missing parameter - targetCurrencyCode"
            ));
        }
        if (StringRate == null || StringRate.isBlank()) {
            resp.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExchangeRatesErrorResponse(
                    SC_BAD_REQUEST,
                    "Missing parameter - rate"
            ));
        }
        if (baseCurrencyCode.length() != 3 || targetCurrencyCode.length() != 3) {
            resp.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExchangeRatesErrorResponse(
                    SC_BAD_REQUEST,
                    "Base currency OR Target currency code must be in ISO 4217 format"
            ));
        }

        BigDecimal rate = null;
        try {
            assert StringRate != null;
            rate = BigDecimal.valueOf(Double.parseDouble(StringRate));
        } catch (NumberFormatException e) {
            resp.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new StringToBigDecimalErrorResponse(
                    SC_BAD_REQUEST,
                    "Incorrect value of rate parameter"
            ));
        }

        try {
            if (currenciesDAO.getByCode(baseCurrencyCode) != null
                    && currenciesDAO.getByCode(targetCurrencyCode) != null) {
                ExchangeRatesModel exchangeRatesModel = new ExchangeRatesModel(
                        currenciesDAO.getByCode(baseCurrencyCode),
                        currenciesDAO.getByCode(targetCurrencyCode),
                        rate
                );

                if (exchangeRatesDAO.getByCode(baseCurrencyCode+targetCurrencyCode) == null) {
                    exchangeRatesDAO.create(exchangeRatesModel);
                    objectMapper.writeValue(resp.getWriter(), exchangeRatesDAO.getByCode(baseCurrencyCode+targetCurrencyCode));
                } else {
                    resp.setStatus(SC_CONFLICT);
                    objectMapper.writeValue(resp.getWriter(), new ExchangeRatesErrorResponse(
                            SC_CONFLICT,
                            "A currency pair with this code already exists"
                    ));
                }
            } else {
                resp.setStatus(SC_NOT_FOUND);
                objectMapper.writeValue(resp.getWriter(), new ExchangeRatesErrorResponse(
                        SC_NOT_FOUND,
                        "One (or both) currency from the currency pair does not exist in the database"
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
