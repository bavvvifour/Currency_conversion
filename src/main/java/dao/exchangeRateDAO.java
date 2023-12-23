package dao;

import model.currenciesModel;
import model.exchangeRatesModel;

import java.sql.*;

public class exchangeRateDAO {
    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:src/main/resources/init.db");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static exchangeRatesModel getexchangeRate(String BaseCode, String TargetCode) {
        BaseCode = BaseCode.toLowerCase();
        TargetCode = TargetCode.toLowerCase();
        exchangeRatesModel exchangeRate = new exchangeRatesModel();
        try (Connection con = getConnection()) {
            String sql = "SELECT e.ID, e.Rate, e.BaseCurrencyId, e.TargetCurrencyId, " +
                    "b.FullName AS BaseFullName, b.Code AS BaseCode, b.Sign AS BaseSign, " +
                    "t.FullName AS TargetFullName, t.Code AS TargetCode, t.Sign AS TargetSign " +
                    "FROM ExchangeRates e " +
                    "JOIN Currencies b ON e.BaseCurrencyId = b.ID " +
                    "JOIN Currencies t ON e.TargetCurrencyId = t.ID " +
                    "WHERE b.Code = ? AND t.code = ?";
            assert con != null;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, BaseCode);
                ps.setString(2, TargetCode);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        exchangeRate = new exchangeRatesModel();
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
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exchangeRate;
    }
    public static void updateExchangeRate(String BaseCode, String TargetCode, double newRate) {
        try (Connection con = getConnection()) {
            String sql = "UPDATE ExchangeRates " +
                    "SET Rate = ? " +
                    "WHERE BaseCurrencyId = (SELECT ID FROM Currencies WHERE Code = ?) " +
                    "AND TargetCurrencyId = (SELECT ID FROM Currencies WHERE Code = ?)";
            assert con != null;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setDouble(1, newRate);
                ps.setString(2, BaseCode);
                ps.setString(3, TargetCode);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
