package test;

import dao.*;
import  model.currenciesModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static dao.currenciesDAO.*;

public class main {
    public static void main(String[] args) {
        String Code = "dscs";
        String FullName = "dvdfdb";
        String Sign = "z";
        Connection con = null;
        try {
            con = getConnection();
            //addCurrency(con, Code, FullName, Sign);
            //List<currenciesModel> cur = getAllCurrencies(con);
            //for (currenciesModel currency : cur) {
            //    System.out.println(currency.getId() + " - " + currency.getCode() + " - " + currency.getFullName() + " - " + currency.getSign());
            //}
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }}
}