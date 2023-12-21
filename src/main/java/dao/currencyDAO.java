package dao;

import model.currenciesModel;

import java.sql.*;

public class currencyDAO {
    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:src/main/resources/init.db");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static currenciesModel getCurrency(String name) {
        name = name.toLowerCase();
        currenciesModel currencies = new currenciesModel();
        try (Connection con = getConnection()) {
            String sql = "SELECT * FROM Currencies WHERE Code = ?";
            assert con != null;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, name);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        currencies = new currenciesModel();
                        currencies.setId(rs.getInt("ID"));
                        currencies.setCode(rs.getString("Code"));
                        currencies.setFullName(rs.getString("FullName"));
                        currencies.setSign(rs.getString("Sign"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currencies;
    }
}
