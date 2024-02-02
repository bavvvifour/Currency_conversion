package dao;

import model.ConvertModel;
import model.CurrenciesModel;
import model.ExchangeRatesModel;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesDAO implements IDAO<ExchangeRatesModel> {

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
    public void create(ExchangeRatesModel exchangeRatesModel) throws SQLException {
        String SQL = "INSERT INTO exchangeRates(BaseCurrencyId, TargetCurrencyId, Rate) VALUES(?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setInt(1, exchangeRatesModel.getBaseCurrencyId().getId());
            preparedStatement.setInt(2, exchangeRatesModel.getTargetCurrencyId().getId());
            preparedStatement.setBigDecimal(3, exchangeRatesModel.getRate());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<ExchangeRatesModel> getAll() throws SQLException {
        String SQL = "select e.ID,\n" +
                "       BaseCurrencyId, b.ID as bID, b.Code as bCode, b.FullName as bFullName, b.Sign as bSign,\n" +
                "       TargetCurrencyId, t.ID as tID, t.Code as tCode, t.FullName as tFullName, t.Sign as tSign,\n" +
                "       Rate from ExchangeRates e\n" +
                "join Currencies b on b.ID = e.BaseCurrencyId\n" +
                "join Currencies t on t.ID = e.TargetCurrencyId";

        List<ExchangeRatesModel> exchangeRate = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");

                int Bid = rs.getInt(("bID"));
                String BFullName = rs.getString("bFullName");
                String BCode = rs.getString("bCode");
                String BSign = rs.getString("bSign");


                int Tid = rs.getInt("tID");
                String TFullName = rs.getString("tFullName");
                String TCode = rs.getString("tCode");
                String TSign = rs.getString("tSign");

                BigDecimal Rate = rs.getBigDecimal("Rate");

                exchangeRate.add(new ExchangeRatesModel(
                        id,
                        new CurrenciesModel(Bid, BCode, BFullName, BSign),
                        new CurrenciesModel(Tid, TCode, TFullName, TSign),
                        Rate
                ));
            }
        }
        return exchangeRate;
    }

    @Override
    public ExchangeRatesModel getByCode(String code) throws SQLException {
        String SQL = "select e.ID,\n" +
                "       BaseCurrencyId, b.ID as bID, b.Code as bCode, b.FullName as bFullName, b.Sign as bSign,\n" +
                "       TargetCurrencyId, t.ID as tID, t.Code as tCode, t.FullName as tFullName, t.Sign as tSign,\n" +
                "       Rate from ExchangeRates e\n" +
                "join Currencies b on b.ID = e.BaseCurrencyId\n" +
                "join Currencies t on t.ID = e.TargetCurrencyId\n" +
                "WHERE b.Code = ? AND t.Code = ?";

        String firstPart = code.substring(0,3);
        String secondPart = code.substring(3);

        ExchangeRatesModel exchangeRate = null;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, firstPart);
            preparedStatement.setString(2, secondPart);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");

                int Bid = rs.getInt(("bID"));
                String BFullName = rs.getString("bFullName");
                String BCode = rs.getString("bCode");
                String BSign = rs.getString("bSign");


                int Tid = rs.getInt("tID");
                String TFullName = rs.getString("tFullName");
                String TCode = rs.getString("tCode");
                String TSign = rs.getString("tSign");

                BigDecimal Rate = rs.getBigDecimal("Rate");

                exchangeRate = new ExchangeRatesModel(id,
                        new CurrenciesModel(Bid, BCode, BFullName, BSign),
                        new CurrenciesModel(Tid, TCode, TFullName, TSign),
                        Rate
                );
            }
        }
        return exchangeRate;
    }

    @Override
    public void update(ExchangeRatesModel exchangeRatesModel) throws SQLException {
        String SQL = "UPDATE ExchangeRates " +
                "SET Rate = ? " +
                "WHERE BaseCurrencyId = ? " +
                "AND TargetCurrencyId = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL)) {

            statement.setBigDecimal(1, exchangeRatesModel.getRate());
            statement.setInt(2, exchangeRatesModel.getBaseCurrencyId().getId());
            statement.setInt(3, exchangeRatesModel.getTargetCurrencyId().getId());

            statement.executeUpdate();
        }
    }

    @Override
    public boolean removeById(Long id) {
        return false;
    }

    @Override
    public boolean removeAll() {
        return false;
    }

    public ConvertModel convertCurrency(String baseCurrency, String targetCurrencyCode, BigDecimal amount) throws SQLException {
        String SQL = "select e.ID,\n" +
                "       BaseCurrencyId, b.ID as bID, b.Code as bCode, b.FullName as bFullName, b.Sign as bSign,\n" +
                "       TargetCurrencyId, t.ID as tID, t.Code as tCode, t.FullName as tFullName, t.Sign as tSign,\n" +
                "       Rate from ExchangeRates e\n" +
                "join Currencies b on b.ID = e.BaseCurrencyId\n" +
                "join Currencies t on t.ID = e.TargetCurrencyId\n" +
                "WHERE b.Code = ? AND t.Code = ?";

        ConvertModel converModel = null;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, baseCurrency);
            preparedStatement.setString(2, targetCurrencyCode);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                BigDecimal rate = rs.getBigDecimal("Rate");

                int Bid = rs.getInt(("bID"));
                String BFullName = rs.getString("bFullName");
                String BCode = rs.getString("bCode");
                String BSign = rs.getString("bSign");


                int Tid = rs.getInt("tID");
                String TFullName = rs.getString("tFullName");
                String TCode = rs.getString("tCode");
                String TSign = rs.getString("tSign");


                BigDecimal result = amount.multiply(rate);

                converModel = new ConvertModel(
                        new CurrenciesModel(Bid, BCode, BFullName, BSign),
                        new CurrenciesModel(Tid, TCode, TFullName, TSign),
                        rate,
                        amount,
                        result
                );
            }
        }
        return converModel;
    }
}
