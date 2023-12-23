package dao;

import model.*;

import java.sql.*;

public class exchangeDAO {
    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:src/main/resources/init.db");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static exchangeRatesModel convertCurrency(String baseCurrencyCode, String targetCurrencyCode, double amount) {
        exchangeRatesModel result = new exchangeRatesModel();
        try (Connection con = getConnection()) {
            String sql = "SELECT e.Rate, " +
                    "b.FullName AS BaseFullName, b.Code AS BaseCode, b.Sign AS BaseSign, " +
                    "t.FullName AS TargetFullName, t.Code AS TargetCode, t.Sign AS TargetSign " +
                    "FROM ExchangeRates e " +
                    "JOIN Currencies b ON e.BaseCurrencyId = b.ID " +
                    "JOIN Currencies t ON e.TargetCurrencyId = t.ID " +
                    "WHERE b.Code = ? AND t.code = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, baseCurrencyCode);
                ps.setString(2, targetCurrencyCode);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        double rate = rs.getDouble("Rate");
                        result.setRate(rate);

                        currenciesModel baseCurrency = new currenciesModel();
                        baseCurrency.setFullName(rs.getString("BaseFullName"));
                        baseCurrency.setCode(rs.getString("BaseCode"));
                        baseCurrency.setSign(rs.getString("BaseSign"));
                        result.setBaseCurrencyid(baseCurrency);

                        currenciesModel targetCurrency = new currenciesModel();
                        targetCurrency.setFullName(rs.getString("TargetFullName"));
                        targetCurrency.setCode(rs.getString("TargetCode"));
                        targetCurrency.setSign(rs.getString("TargetSign"));
                        result.setTargetCurrencyid(targetCurrency);

                        result.setAmount(amount);
                        result.setConvertedAmount(amount * rate);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
