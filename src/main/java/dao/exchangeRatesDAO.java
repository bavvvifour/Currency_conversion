package dao;

import model.currenciesModel;
import model.exchangeRatesModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class exchangeRatesDAO {
    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:src/main/resources/init.db");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void addExchangeRate(double Rate, int BaseCurrencyid, int TargetCurrencyid) {
        Connection con = getConnection();
        try (con) {
            String sql = "INSERT INTO ExchangeRates (Rate, BaseCurrencyid, TargetCurrencyid) " +
                    "VALUES (?, ?, ?)";
            assert con != null;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setDouble(1, Rate);
                ps.setInt(2, BaseCurrencyid);
                ps.setInt(3, TargetCurrencyid);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static List<exchangeRatesModel> getAllExchangeRates() {
        List<exchangeRatesModel> exchangeRates = new ArrayList<>();
        Connection con = getConnection();
        try (con) {
            String sql = "SELECT e.ID, e.Rate, e.BaseCurrencyId, e.TargetCurrencyId, " +
                    "b.FullName AS BaseFullName, b.Code AS BaseCode, b.Sign AS BaseSign, " +
                    "t.FullName AS TargetFullName, t.Code AS TargetCode, t.Sign AS TargetSign " +
                    "FROM ExchangeRates e " +
                    "JOIN Currencies b ON e.BaseCurrencyId = b.ID " +
                    "JOIN Currencies t ON e.TargetCurrencyId = t.ID";
            assert con != null;
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    exchangeRatesModel exchangeRate = new exchangeRatesModel();
                    exchangeRate.setID(rs.getInt("ID"));
                    exchangeRate.setRate(rs.getDouble("Rate"));

                    currenciesModel BaseCurrency = new currenciesModel();
                    BaseCurrency.setId(rs.getInt("BaseCurrencyId"));
                    BaseCurrency.setCode(rs.getString("BaseCode"));
                    BaseCurrency.setFullName(rs.getString("BaseFullName"));
                    BaseCurrency.setSign(rs.getString("BaseSign"));

                    currenciesModel TargetCurrency = new currenciesModel();
                    TargetCurrency.setId(rs.getInt("TargetCurrencyId"));
                    TargetCurrency.setCode(rs.getString("TargetCode"));
                    TargetCurrency.setFullName(rs.getString("TargetFullName"));
                    TargetCurrency.setSign(rs.getString("TargetSign"));

                    exchangeRate.setBaseCurrencyid(BaseCurrency);
                    exchangeRate.setTargetCurrencyid(TargetCurrency);

                    exchangeRates.add(exchangeRate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exchangeRates;
    }
}
