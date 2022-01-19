package ir.bootcamp.bank.repositories;

import ir.bootcamp.bank.model.Account;
import ir.bootcamp.bank.model.Card;
import ir.bootcamp.bank.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ir.bootcamp.bank.repositories.Const.*;

public class CardRepository extends JdbcRepository<Card> {

    public CardRepository(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    protected void createTable() throws SQLException {
        String query = "create table if not exists " + CARD_COLUMN_NUMBER + "" +
                "(" +
                "    " + CARD_COLUMN_ID + "       serial primary key," +
                "    " + CARD_COLUMN_NUMBER + "     char(16) not null ," +
                "    " + CARD_COLUMN_CVV2 + " smallint not null ," +
                "    " + CARD_COLUMN_PASSWORD + " varchar(255) not null ," +
                "    " + CARD_COLUMN_EXPIRE_DATE + " date not null ," +
                "    " + CARD_COLUMN_ACCOUNT_ID + " int not null ," +
                "    " + CARD_COLUMN_STATUS + " bit not null" +
                "" +
                ");";
        connection.createStatement().execute(query);
    }

    @Override
    public int add(Card card) throws SQLException {
        String sql = "insert into " + CARD_TABLE_NAME + " values (DEFAULT, ?, ?, ?,?,?,?) returning " + CARD_COLUMN_ID + "";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, card.cardNumber());
        preparedStatement.setString(2, card.cvv2());
        preparedStatement.setString(3, card.password());
        preparedStatement.setDate(4, card.expireDate());
        preparedStatement.setInt(6, card.account().id());
        preparedStatement.setShort(6, card.status());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(ACCOUNT_COLUMN_ID);
        }
        return -1;
    }

    @Override
    public Card find(int id) throws SQLException {
        String sql = "select * from " + CARD_TABLE_NAME +
                " inner join " + ACCOUNT_TABLE_NAME +
                " on " + CARD_TABLE_NAME + "." + CARD_COLUMN_ACCOUNT_ID + " = " + ACCOUNT_TABLE_NAME + "." + ACCOUNT_COLUMN_ID +
                " inner join " + CUSTOMER_TABLE_NAME +
                " on " + ACCOUNT_TABLE_NAME + "." + ACCOUNT_COLUMN_CUSTOMER_ID + " = " + CUSTOMER_TABLE_NAME + "." + CUSTOMER_COLUMN_ID +
                " where " + CARD_COLUMN_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }


    public Card find(String cardNumber) throws SQLException {
        String sql = "select * from " + CARD_TABLE_NAME +
                " inner join " + ACCOUNT_TABLE_NAME +
                " on " + CARD_TABLE_NAME + "." + CARD_COLUMN_ACCOUNT_ID + " = " + ACCOUNT_TABLE_NAME + "." + ACCOUNT_COLUMN_ID +
                " inner join " + CUSTOMER_TABLE_NAME +
                " on " + ACCOUNT_TABLE_NAME + "." + ACCOUNT_COLUMN_CUSTOMER_ID + " = " + CUSTOMER_TABLE_NAME + "." + CUSTOMER_COLUMN_ID +
                " where " + CARD_COLUMN_NUMBER + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, cardNumber);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    @Override
    public List<Card> findAll() throws SQLException {
        String sql = "select * from " + CARD_TABLE_NAME +
                " inner join " + ACCOUNT_TABLE_NAME +
                " on " + CARD_TABLE_NAME + "." + CARD_COLUMN_ACCOUNT_ID + " = " + ACCOUNT_TABLE_NAME + "." + ACCOUNT_COLUMN_ID +
                " inner join " + CUSTOMER_TABLE_NAME +
                " on " + ACCOUNT_TABLE_NAME + "." + ACCOUNT_COLUMN_CUSTOMER_ID + " = " + CUSTOMER_TABLE_NAME + "." + CUSTOMER_COLUMN_ID;
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        return mapToList(resultSet);
    }

    @Override
    public int update(Card card) throws SQLException {
        String sql = "update " + CARD_TABLE_NAME + " set " +
                CARD_COLUMN_NUMBER + " = ?, " +
                CARD_COLUMN_CVV2 + " = ?, " +
                CARD_COLUMN_PASSWORD + " = ?, " +
                CARD_COLUMN_EXPIRE_DATE + " = ?, " +
                CARD_COLUMN_ACCOUNT_ID + " = ?, " +
                CARD_COLUMN_STATUS + " = ? " +
                "where " + CARD_COLUMN_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, card.cardNumber());
        preparedStatement.setString(2, card.cvv2());
        preparedStatement.setString(3, card.password());
        preparedStatement.setDate(4, card.expireDate());
        preparedStatement.setInt(4, card.account().id());
        preparedStatement.setShort(4, card.status());
        return preparedStatement.executeUpdate();
    }

    @Override
    public int delete(int id) throws SQLException {
        String sql = "delete from " + CARD_TABLE_NAME + " where " + CARD_COLUMN_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeUpdate();
    }

    @Override
    protected Card mapTo(ResultSet resultSet) throws SQLException {
        if (!resultSet.next())
            return null;
        return new Card(resultSet.getInt(CARD_COLUMN_ID),
                resultSet.getString(CARD_COLUMN_NUMBER),
                resultSet.getString(CARD_COLUMN_CVV2),
                resultSet.getDate(CARD_COLUMN_EXPIRE_DATE),
                resultSet.getString(CARD_COLUMN_PASSWORD),
                new Account(
                        resultSet.getInt(ACCOUNT_COLUMN_ID),
                        resultSet.getString(ACCOUNT_COLUMN_NUMBER),
                        resultSet.getLong(ACCOUNT_COLUMN_AMOUNT),
                        new Customer(
                                resultSet.getInt(CUSTOMER_COLUMN_ID),
                                resultSet.getString(CUSTOMER_COLUMN_NAME),
                                resultSet.getString(CUSTOMER_COLUMN_NATIONAL_CODE),
                                resultSet.getString(CUSTOMER_COLUMN_PHONE)
                        ))
                , resultSet.getByte(CARD_COLUMN_STATUS)
        );
    }

    @Override
    protected List<Card> mapToList(ResultSet resultSet) throws SQLException {
        List<Card> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(new Card(resultSet.getInt(CARD_COLUMN_ID),
                    resultSet.getString(CARD_COLUMN_NUMBER),
                    resultSet.getString(CARD_COLUMN_CVV2),
                    resultSet.getDate(CARD_COLUMN_EXPIRE_DATE),
                    resultSet.getString(CARD_COLUMN_PASSWORD),
                    new Account(
                            resultSet.getInt(ACCOUNT_COLUMN_ID),
                            resultSet.getString(ACCOUNT_COLUMN_NUMBER),
                            resultSet.getLong(ACCOUNT_COLUMN_AMOUNT),
                            new Customer(
                                    resultSet.getInt(CUSTOMER_COLUMN_ID),
                                    resultSet.getString(CUSTOMER_COLUMN_NAME),
                                    resultSet.getString(CUSTOMER_COLUMN_NATIONAL_CODE),
                                    resultSet.getString(CUSTOMER_COLUMN_PHONE)
                            ))
                    , resultSet.getByte(CARD_COLUMN_STATUS)
            ));
        }
        return result;
    }

}
