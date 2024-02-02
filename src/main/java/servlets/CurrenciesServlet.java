package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CurrenciesDAO;
import exception.CurrencyErrorResponse;
import model.CurrenciesModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet(urlPatterns = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrenciesDAO currenciesDAO = new CurrenciesDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            objectMapper.writeValue(resp.getWriter(), currenciesDAO.getAll());
        } catch (SQLException e) {
            resp.setStatus(SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new CurrencyErrorResponse(
                    SC_INTERNAL_SERVER_ERROR,
                    "Something happened with the database, try again later!"
            ));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code").toUpperCase();
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");

        if (name == null || name.isBlank()) {
            resp.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new CurrencyErrorResponse(
                    SC_BAD_REQUEST,
                    "Missing parameter - name"
            ));
        }
        if (code.isBlank()) {
            resp.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new CurrencyErrorResponse(
                    SC_BAD_REQUEST,
                    "Missing parameter - code"
            ));
        }
        if (sign == null || sign.isBlank()) {
            resp.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new CurrencyErrorResponse(
                    SC_BAD_REQUEST,
                    "Missing parameter - sign"
            ));
        }
        if (code.length()!=3) {
            resp.setStatus(SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new CurrencyErrorResponse(
                    SC_BAD_REQUEST,
                    "Currency code must be in ISO 4217 format"
            ));
        }

        try {
            if (currenciesDAO.getByCode(code) == null) {
                currenciesDAO.create(new CurrenciesModel(code, name, sign));
                objectMapper.writeValue(resp.getWriter(), currenciesDAO.getAll());
            } else {
                resp.setStatus(SC_CONFLICT);
                objectMapper.writeValue(resp.getWriter(), new CurrencyErrorResponse(
                        SC_CONFLICT,
                        "A currency with this code already exists"
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
