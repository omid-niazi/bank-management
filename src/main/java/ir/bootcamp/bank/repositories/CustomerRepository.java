package ir.bootcamp.bank.repositories;

import ir.bootcamp.bank.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ir.bootcamp.bank.util.DatabaseConstants.*;

public class CustomerRepository extends JdbcRepository<Customer> {


    public CustomerRepository(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    protected void createTable() throws SQLException {
        String query = "create table if not exists " + CUSTOMER_TABLE_NAME + "" +
                "(" +
                "    " + CUSTOMER_COLUMN_ID + "       serial primary key," +
                "    " + CUSTOMER_COLUMN_NAME + "     varchar(255) not null ," +
                "    " + CUSTOMER_COLUMN_NATIONAL_CODE + " varchar(255) unique not null ," +
                "    " + CUSTOMER_COLUMN_PHONE + " varchar(255) not null" +
                "" +
                ");";
        connection.createStatement().execute(query);
    }

    @Override
    public int add(Customer customer) throws SQLException {
        String sql = "insert into " + CUSTOMER_TABLE_NAME + " values (DEFAULT, ?, ?, ?) returning " + CUSTOMER_COLUMN_ID + "";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, customer.name());
        preparedStatement.setString(2, customer.nationalCode());
        preparedStatement.setString(3, customer.phone());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(CUSTOMER_COLUMN_ID);
        }
        return -1;
    }

    @Override
    public Customer find(int id) throws SQLException {
        String sql = "select * from " + CUSTOMER_TABLE_NAME + " where " + CUSTOMER_COLUMN_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    public Customer find(String nationalCode) throws SQLException {
        String sql = "select * from " + CUSTOMER_TABLE_NAME + " where " + CUSTOMER_COLUMN_NATIONAL_CODE+ " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, nationalCode);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    @Override
    public List<Customer> findAll() throws SQLException {
        String sql = "select * from " + CUSTOMER_TABLE_NAME + "";
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        return mapToList(resultSet);
    }

    @Override
    public int update(Customer customer) throws SQLException {
        String sql = "update " + CUSTOMER_TABLE_NAME + " set " + CUSTOMER_COLUMN_NAME + " = ?, " + CUSTOMER_COLUMN_NATIONAL_CODE + " = ?, " + CUSTOMER_COLUMN_PHONE + " = ? where " + CUSTOMER_COLUMN_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, customer.name());
        preparedStatement.setString(2, customer.nationalCode());
        preparedStatement.setString(3, customer.phone());
        preparedStatement.setInt(4, customer.id());
        return preparedStatement.executeUpdate();
    }

    @Override
    public int delete(int id) throws SQLException {
        String sql = "delete from " + CUSTOMER_TABLE_NAME + " where " + CUSTOMER_COLUMN_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeUpdate();
    }

    @Override
    protected Customer mapTo(ResultSet resultSet) throws SQLException {
        if (!resultSet.next())
            return null;
        return new Customer(
                resultSet.getInt(CUSTOMER_COLUMN_ID),
                resultSet.getString(CUSTOMER_COLUMN_NAME),
                resultSet.getString(CUSTOMER_COLUMN_NATIONAL_CODE),
                resultSet.getString(CUSTOMER_COLUMN_PHONE)
        );
    }

    @Override
    protected List<Customer> mapToList(ResultSet resultSet) throws SQLException {
        List<Customer> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(new Customer(
                    resultSet.getInt(CUSTOMER_COLUMN_ID),
                    resultSet.getString(CUSTOMER_COLUMN_NAME),
                    resultSet.getString(CUSTOMER_COLUMN_NATIONAL_CODE),
                    resultSet.getString(CUSTOMER_COLUMN_PHONE)));
        }
        return result;
    }

}
