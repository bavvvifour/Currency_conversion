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
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@WebServlet(urlPatterns = "/exchangeRate/*")
public class exchangeRateServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExchangeRatesDAO exchangeRatesDAO = new ExchangeRatesDAO();
    private final CurrenciesDAO currenciesDAO = new CurrenciesDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();

        String codeCurrency = (requestURI).substring(requestURI.lastIndexOf('/') + 1).toUpperCase();

        if (codeCurrency.length() != 6) {
            resp.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new CurrencyErrorResponse(
                    SC_BAD_REQUEST,
                    "The currency code is missing from the address"
            ));
        }

        try {
            if (exchangeRatesDAO.getByCode(codeCurrency) != null) {
                objectMapper.writeValue(resp.getWriter(), exchangeRatesDAO.getByCode(codeCurrency));
            } else {
                resp.setStatus(SC_NOT_FOUND);
                objectMapper.writeValue(resp.getWriter(), new ExchangeRatesErrorResponse(
                        SC_NOT_FOUND,
                        "The exchange rate for the pair was not found"
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

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (!method.equals("PATCH")) {
            super.service(req, resp);
        }
        this.doPatch(req, resp);
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String StringRate = req.getParameter("rate");

        String requestURI = req.getRequestURI();
        String codeCurrency = (requestURI).substring(requestURI.lastIndexOf('/') + 1).toUpperCase();

        if (codeCurrency.length() != 6) {
            resp.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new CurrencyErrorResponse(
                    SC_BAD_REQUEST,
                    "The required form field is missing"
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
            if (exchangeRatesDAO.getByCode(codeCurrency) != null) {
                exchangeRatesDAO.update(
                        new ExchangeRatesModel(
                                currenciesDAO.getByCode(codeCurrency.substring(0,3)),
                                currenciesDAO.getByCode(codeCurrency.substring(3)),
                                rate
                        ));
                objectMapper.writeValue(resp.getWriter(), exchangeRatesDAO.getByCode(codeCurrency));
            } else {
                resp.setStatus(SC_NOT_FOUND);
                objectMapper.writeValue(resp.getWriter(), new ExchangeRatesErrorResponse(
                        SC_NOT_FOUND,
                        "The exchange rate for the pair was not found"
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
