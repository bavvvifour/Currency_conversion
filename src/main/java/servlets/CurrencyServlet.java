package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CurrenciesDAO;
import exception.CurrencyErrorResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static javax.servlet.http.HttpServletResponse.*;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@WebServlet(urlPatterns = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrenciesDAO currenciesDAO = new CurrenciesDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();

        String codeCurrency = (requestURI).substring(requestURI.lastIndexOf('/') + 1).toUpperCase();

        if (codeCurrency.isEmpty()) {
            resp.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new CurrencyErrorResponse(
                    SC_BAD_REQUEST,
                    "The currency code is missing from the address"
            ));
        }

        try {
            if (currenciesDAO.getByCode(codeCurrency) != null) {
                objectMapper.writeValue(resp.getWriter(), currenciesDAO.getByCode(codeCurrency));
            } else {
                resp.setStatus(SC_NOT_FOUND);
                objectMapper.writeValue(resp.getWriter(), new CurrencyErrorResponse(
                        SC_NOT_FOUND,
                        "The currency was not found"
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
