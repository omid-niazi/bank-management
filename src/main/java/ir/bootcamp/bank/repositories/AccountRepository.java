package ir.bootcamp.bank.repositories;

import ir.bootcamp.bank.model.Account;
import ir.bootcamp.bank.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ir.bootcamp.bank.util.DatabaseConstants.*;

public class AccountRepository extends JdbcRepository<Account> {

    public AccountRepository(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    protected void createTable() throws SQLException {
        String query = "create table if not exists " + ACCOUNT_TABLE_NAME + "" +
                "(" +
                "    " + ACCOUNT_COLUMN_ID + "       serial primary key," +
                "    " + ACCOUNT_COLUMN_NUMBER + "     varchar(255) unique not null ," +
                "    " + ACCOUNT_COLUMN_AMOUNT + " bigint  not null ," +
                "    " + ACCOUNT_COLUMN_CUSTOMER_ID + " int not null" +
                "" +
                ");";
        connection.createStatement().execute(query);
    }

    @Override
    public int add(Account account) throws SQLException {
        String sql = "insert into " + ACCOUNT_TABLE_NAME + " values (DEFAULT, ?, ?, ?) returning " + ACCOUNT_COLUMN_ID + "";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, account.accountNumber());
        preparedStatement.setLong(2, account.amount());
        preparedStatement.setInt(3, account.customer().id());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(ACCOUNT_COLUMN_ID);
        }
        return -1;
    }

    @Override
    public Account find(int id) throws SQLException {
        String sql = "select * from " + ACCOUNT_TABLE_NAME +
                " inner join " + CUSTOMER_TABLE_NAME +
                " on " + ACCOUNT_TABLE_NAME + "." + ACCOUNT_COLUMN_CUSTOMER_ID + " = " + CUSTOMER_TABLE_NAME + "." + CUSTOMER_COLUMN_ID +
                " where " + ACCOUNT_COLUMN_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    public Account findByAccountNumber(String accountNumber) throws SQLException {
        String sql = "select * from " + ACCOUNT_TABLE_NAME +
                " inner join " + CUSTOMER_TABLE_NAME +
                " on " + ACCOUNT_TABLE_NAME + "." + ACCOUNT_COLUMN_CUSTOMER_ID + " = " + CUSTOMER_TABLE_NAME + "." + CUSTOMER_COLUMN_ID +
                " where " + ACCOUNT_COLUMN_NUMBER + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, accountNumber);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    public List<Account> findByCustomerId(int customerId) throws SQLException {
        String sql = "select * from " + ACCOUNT_TABLE_NAME +
                " inner join " + CUSTOMER_TABLE_NAME +
                " on " + ACCOUNT_TABLE_NAME + "." + ACCOUNT_COLUMN_CUSTOMER_ID + " = " + CUSTOMER_TABLE_NAME + "." + CUSTOMER_COLUMN_ID +
                " where " + ACCOUNT_COLUMN_CUSTOMER_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,customerId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapToList(resultSet);
    }

    @Override
    public List<Account> findAll() throws SQLException {
        String sql = "select * from " + ACCOUNT_TABLE_NAME +
                " inner join " + CUSTOMER_TABLE_NAME +
                " on " + ACCOUNT_TABLE_NAME + "." + ACCOUNT_COLUMN_CUSTOMER_ID + " = " + CUSTOMER_TABLE_NAME + "." + CUSTOMER_COLUMN_ID;
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        return mapToList(resultSet);
    }

    @Override
    public int update(Account account) throws SQLException {
        String sql = "update " + ACCOUNT_TABLE_NAME + " set " + ACCOUNT_COLUMN_NUMBER + " = ?, " + ACCOUNT_COLUMN_AMOUNT + " = ?, " + ACCOUNT_COLUMN_CUSTOMER_ID + " = ? where " + ACCOUNT_COLUMN_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, account.accountNumber());
        preparedStatement.setLong(2, account.amount());
        preparedStatement.setInt(3, account.customer().id());
        preparedStatement.setInt(4, account.id());
        return preparedStatement.executeUpdate();
    }

    @Override
    public int delete(int id) throws SQLException {
        String sql = "delete from " + ACCOUNT_TABLE_NAME + " where " + ACCOUNT_COLUMN_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeUpdate();
    }

    @Override
    protected Account mapTo(ResultSet resultSet) throws SQLException {
        if (!resultSet.next())
            return null;
        return new Account(
                resultSet.getInt(ACCOUNT_COLUMN_ID),
                resultSet.getString(ACCOUNT_COLUMN_NUMBER),
                resultSet.getLong(ACCOUNT_COLUMN_AMOUNT),
                new Customer(
                        resultSet.getInt(CUSTOMER_COLUMN_ID),
                        resultSet.getString(CUSTOMER_COLUMN_NAME),
                        resultSet.getString(CUSTOMER_COLUMN_NATIONAL_CODE),
                        resultSet.getString(CUSTOMER_COLUMN_PHONE)
                )
        );
    }

    @Override
    protected List<Account> mapToList(ResultSet resultSet) throws SQLException {
        List<Account> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(new Account(
                    resultSet.getInt(ACCOUNT_COLUMN_ID),
                    resultSet.getString(ACCOUNT_COLUMN_NUMBER),
                    resultSet.getLong(ACCOUNT_COLUMN_AMOUNT),
                    new Customer(
                            resultSet.getInt(CUSTOMER_COLUMN_ID),
                            resultSet.getString(CUSTOMER_COLUMN_NAME),
                            resultSet.getString(CUSTOMER_COLUMN_NATIONAL_CODE),
                            resultSet.getString(CUSTOMER_COLUMN_PHONE)
                    )
            ));
        }
        return result;
    }

}
