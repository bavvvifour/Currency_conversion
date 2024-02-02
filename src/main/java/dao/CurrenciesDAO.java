package dao;

import model.CurrenciesModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrenciesDAO implements IDAO<CurrenciesModel> {

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/init.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }


    @Override
    public void create(CurrenciesModel currenciesModel) throws SQLException {
        String SQL = "INSERT INTO Currencies (Code, FullName, Sign) VALUES(?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, currenciesModel.getCode());
            preparedStatement.setString(2, currenciesModel.getFullName());
            preparedStatement.setString(3, currenciesModel.getSign());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<CurrenciesModel> getAll() throws SQLException {
        String SQL = "SELECT * FROM Currencies";

        List<CurrenciesModel> currenciesModels = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                String FullName = rs.getString("FullName");
                String Code = rs.getString("Code");
                String Sign = rs.getString("Sign");
                currenciesModels.add(new CurrenciesModel(id, FullName, Code, Sign));
            }
        }
        return currenciesModels;
    }

    @Override
    public CurrenciesModel getByCode(String code) throws SQLException {
        String SQL = "SELECT * FROM Currencies WHERE Code = ?";

        CurrenciesModel currenciesModel = null;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, code);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                String FullName = rs.getString("FullName");
                String Code = rs.getString("Code");
                String Sign = rs.getString("Sign");
                currenciesModel = new CurrenciesModel(id, FullName, Code, Sign);
            }
        }
        return currenciesModel;
    }

    @Override
    public void update(CurrenciesModel currenciesModel) throws SQLException {
    }

    @Override
    public boolean removeById(Long id) {
        return false;
    }

    @Override
    public boolean removeAll() {
        return false;
    }
}
