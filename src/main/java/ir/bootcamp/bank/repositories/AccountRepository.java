package ir.bootcamp.bank.repositories;

import ir.bootcamp.bank.dbutil.Condition;
import ir.bootcamp.bank.dbutil.Query;
import ir.bootcamp.bank.model.Account;
import ir.bootcamp.bank.model.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ir.bootcamp.bank.dbutil.DatabaseConstants.*;

public class AccountRepository extends JdbcRepository<Account> {

    public AccountRepository(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    protected void createTable() throws SQLException {
        String query = "create table if not exists " + ACCOUNT_TABLE_NAME + "" + "(" + "    " + ACCOUNT_COLUMN_ID + "       serial primary key," + "    " + ACCOUNT_COLUMN_NUMBER + "     varchar(255) unique not null ," + "    " + ACCOUNT_COLUMN_AMOUNT + " bigint  not null ," + "    " + ACCOUNT_COLUMN_CUSTOMER_ID + " int not null" + "" + ");";
        connection.createStatement().execute(query);
    }

    @Override
    public int add(Account account) throws SQLException {
        ResultSet resultSet = statementExecutor.executeQuery(new Query.Builder().insertInto(ACCOUNT_TABLE_NAME).setValues(account.accountNumber(), account.amount(), account.customer().id()).returnColumns(ACCOUNT_COLUMN_ID).build());
        if (resultSet.next()) {
            return resultSet.getInt(ACCOUNT_COLUMN_ID);
        }
        return -1;
    }

    @Override
    public Account find(int id) throws SQLException {
        ResultSet resultSet = statementExecutor.executeQuery(new Query.Builder().select("*").from(ACCOUNT_TABLE_NAME).innerJoin(CUSTOMER_TABLE_NAME).on(ACCOUNT_TABLE_NAME + "." + ACCOUNT_COLUMN_CUSTOMER_ID + " = " + CUSTOMER_TABLE_NAME + "." + CUSTOMER_COLUMN_ID).where(Condition.equalsTo(ACCOUNT_COLUMN_ID, id)).build());
        return mapTo(resultSet);
    }

    public Account findByAccountNumber(String accountNumber) throws SQLException {
        ResultSet resultSet = statementExecutor.executeQuery(new Query.Builder().select("*").from(ACCOUNT_TABLE_NAME).innerJoin(CUSTOMER_TABLE_NAME).on(ACCOUNT_TABLE_NAME + "." + ACCOUNT_COLUMN_CUSTOMER_ID + " = " + CUSTOMER_TABLE_NAME + "." + CUSTOMER_COLUMN_ID).where(Condition.equalsTo(ACCOUNT_COLUMN_NUMBER, accountNumber)).build());
        return mapTo(resultSet);
    }

    public List<Account> findByCustomerId(int customerId) throws SQLException {
        ResultSet resultSet = statementExecutor.executeQuery(new Query.Builder().select("*").from(ACCOUNT_TABLE_NAME).innerJoin(CUSTOMER_TABLE_NAME).on(ACCOUNT_TABLE_NAME + "." + ACCOUNT_COLUMN_CUSTOMER_ID + " = " + CUSTOMER_TABLE_NAME + "." + CUSTOMER_COLUMN_ID).where(Condition.equalsTo(ACCOUNT_COLUMN_CUSTOMER_ID, customerId)).build());
        return mapToList(resultSet);
    }

    @Override
    public List<Account> findAll() throws SQLException {
        ResultSet resultSet = statementExecutor.executeQuery(new Query.Builder().select("*").from(ACCOUNT_TABLE_NAME).innerJoin(CUSTOMER_TABLE_NAME).on(ACCOUNT_TABLE_NAME + "." + ACCOUNT_COLUMN_CUSTOMER_ID + " = " + CUSTOMER_TABLE_NAME + "." + CUSTOMER_COLUMN_ID).build());
        return mapToList(resultSet);
    }

    @Override
    public int update(Account account) throws SQLException {
        return statementExecutor.executeUpdate(new Query.Builder().update(ACCOUNT_TABLE_NAME).set(Map.of(ACCOUNT_COLUMN_NUMBER, account.accountNumber(), ACCOUNT_COLUMN_AMOUNT, account.amount(), ACCOUNT_COLUMN_CUSTOMER_ID, account.customer().id())).where(Condition.equalsTo(ACCOUNT_COLUMN_ID, account.id())).build());
    }

    @Override
    public int delete(int id) throws SQLException {
        return statementExecutor.executeUpdate(new Query.Builder().deleteFrom(ACCOUNT_TABLE_NAME).where(Condition.equalsTo(ACCOUNT_COLUMN_ID, id)).build());
    }

    @Override
    protected Account mapTo(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) return null;
        return new Account(resultSet.getInt(ACCOUNT_COLUMN_ID), resultSet.getString(ACCOUNT_COLUMN_NUMBER), resultSet.getLong(ACCOUNT_COLUMN_AMOUNT), new Customer(resultSet.getInt(CUSTOMER_COLUMN_ID), resultSet.getString(CUSTOMER_COLUMN_NAME), resultSet.getString(CUSTOMER_COLUMN_NATIONAL_CODE), resultSet.getString(CUSTOMER_COLUMN_PHONE)));
    }

    @Override
    protected List<Account> mapToList(ResultSet resultSet) throws SQLException {
        List<Account> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(new Account(resultSet.getInt(ACCOUNT_COLUMN_ID), resultSet.getString(ACCOUNT_COLUMN_NUMBER), resultSet.getLong(ACCOUNT_COLUMN_AMOUNT), new Customer(resultSet.getInt(CUSTOMER_COLUMN_ID), resultSet.getString(CUSTOMER_COLUMN_NAME), resultSet.getString(CUSTOMER_COLUMN_NATIONAL_CODE), resultSet.getString(CUSTOMER_COLUMN_PHONE))));
        }
        return result;
    }

}
