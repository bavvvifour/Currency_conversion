package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.currenciesModel;

//CREATE, READ, UPDATE, DELETE

public class currenciesDAO {
    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:src/main/resources/CurrenciesDB.db");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void addCurrency(String Code, String FullName, String Sign) {
        Connection con = getConnection();
        try (con) {
            String sql = "INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?)";
            assert con != null;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, Code);
                ps.setString(2, FullName);
                ps.setString(3, Sign);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<currenciesModel> getAllCurrencies() {
        List<currenciesModel> currencies = new ArrayList<>();
        Connection con = getConnection();
        try (con) {
            String sql = "SELECT * FROM Currencies";
            assert con != null;
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    currenciesModel currency = new currenciesModel();
                    currency.setId(rs.getInt("ID"));
                    currency.setCode(rs.getString("Code"));
                    currency.setFullName(rs.getString("FullName"));
                    currency.setSign(rs.getString("Sign"));
                    currencies.add(currency);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currencies;
    }

    // ИЗМЕНИТЬ ЗНАЧЕНИЕ ВАЛЮТЫ
//    public void updateCurrencies(currenciesModel currenciesModel) {
//        Connection con = getConnection();
//        try (con) {
//            PreparedStatement updateStatement = con.prepareStatement("UPDATE Currencies " +
//                    "SET Code = ?, FullName = ?, Sign = ? WHERE ID = ?");
//            updateStatement.setString(1, currenciesModel.getCode());
//            updateStatement.setString(2, currenciesModel.getFullName());
//            updateStatement.setString(3, currenciesModel.getSign());
//            updateStatement.setInt(4, currenciesModel.getId());
//            updateStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    // УДАЛИТЬ ВАЛЮТУ
//    public void deleteCurrencies(int userId) {
//        Connection con = getConnection();
//        try (con) {
//            PreparedStatement deleteStatement = con.prepareStatement("DELETE FROM Currencies WHERE id = ?");
//            deleteStatement.setInt(1, userId);
//            deleteStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    // ЗАКРЫТЬ СОЕДИНЕНИЕ
//    public void closeConnection() {
//        Connection con = getConnection();
//        try (con) {
//            if (con != null) {
//                con.close();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}