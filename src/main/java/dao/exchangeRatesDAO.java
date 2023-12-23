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
    public static void addExchangeRate(String baseCurrencyCode, String targetCurrencyCode, double rate) {
        try (Connection con = getConnection()) {
            String sqlBaseCur = "SELECT ID FROM Currencies WHERE Code = ?";
            assert con != null;
            try (PreparedStatement baseCurrencyIdStmt = con.prepareStatement(sqlBaseCur)) {
                baseCurrencyIdStmt.setString(1, baseCurrencyCode);
                try (ResultSet baseCurrencyIdRs = baseCurrencyIdStmt.executeQuery()) {
                    if (baseCurrencyIdRs.next()) {
                        int baseCurrencyId = baseCurrencyIdRs.getInt("ID");

                        String sqlTargetCur = "SELECT ID FROM Currencies WHERE Code = ?";
                        try (PreparedStatement targetCurrencyIdStmt = con.prepareStatement(sqlTargetCur)) {
                            targetCurrencyIdStmt.setString(1, targetCurrencyCode);
                            try (ResultSet targetCurrencyIdRs = targetCurrencyIdStmt.executeQuery()) {
                                if (targetCurrencyIdRs.next()) {
                                    int targetCurrencyId = targetCurrencyIdRs.getInt("ID");

                                    String sql = "INSERT INTO ExchangeRates(Rate, BaseCurrencyId, TargetCurrencyId) VALUES(?, ?, ?)";
                                    try (PreparedStatement ps = con.prepareStatement(sql)) {
                                        ps.setDouble(1, rate);
                                        ps.setInt(2, baseCurrencyId);
                                        ps.setInt(3, targetCurrencyId);
                                        ps.executeUpdate();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//
//    public void readExchangeRate(int exchangeRateId) {
//        String sql = "SELECT * FROM ExchangeRates WHERE ID = ?";
//        try (Connection conn = this.connect();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, exchangeRateId);
//            ResultSet rs = pstmt.executeQuery();
//            while (rs.next()) {
//                System.out.println("ID: " + rs.getInt("ID") + ", Rate: " + rs.getDouble("Rate") + ", BaseCurrencyId: " + rs.getInt("BaseCurrencyId") + ", TargetCurrencyId: " + rs.getInt("TargetCurrencyId"));
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    public void updateExchangeRate(int exchangeRateId, double newRate) {
//        String sql = "UPDATE ExchangeRates SET Rate = ? WHERE ID = ?";
//        try (Connection conn = this.connect();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setDouble(1, newRate);
//            pstmt.setInt(2, exchangeRateId);
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    public void deleteExchangeRate(int exchangeRateId) {
//        String sql = "DELETE FROM ExchangeRates WHERE ID = ?";
//        try (Connection conn = this.connect();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, exchangeRateId);
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
}
