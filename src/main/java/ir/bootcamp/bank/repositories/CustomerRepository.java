package ir.bootcamp.bank.repositories;

import ir.bootcamp.bank.dbutil.Condition;
import ir.bootcamp.bank.dbutil.Query;
import ir.bootcamp.bank.model.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ir.bootcamp.bank.dbutil.DatabaseConstants.*;

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
        ResultSet resultSet = statementExecutor.executeQuery(new Query.Builder()
                .insertInto(CUSTOMER_TABLE_NAME)
                .setValues(customer.name(), customer.nationalCode(), customer.phone())
                .returnColumns(CUSTOMER_COLUMN_ID)
                .build());
        if (resultSet.next()) {
            return resultSet.getInt(CUSTOMER_COLUMN_ID);
        }
        return -1;
    }

    @Override
    public Customer find(int id) throws SQLException {
        ResultSet resultSet =
                statementExecutor.executeQuery(new Query.Builder()
                        .select("*")
                        .from(CUSTOMER_TABLE_NAME)
                        .where(Condition.equalsTo(CUSTOMER_COLUMN_ID, id))
                        .build());
        return mapTo(resultSet);
    }

    public Customer find(String nationalCode) throws SQLException {
        ResultSet resultSet =
                statementExecutor.executeQuery(new Query.Builder()
                        .select("*")
                        .from(CUSTOMER_TABLE_NAME)
                        .where(Condition.equalsTo(CUSTOMER_COLUMN_NATIONAL_CODE, nationalCode))
                        .build());
        return mapTo(resultSet);
    }

    @Override
    public List<Customer> findAll() throws SQLException {
        ResultSet resultSet =
                statementExecutor.executeQuery(new Query.Builder()
                        .select("*")
                        .from(CUSTOMER_TABLE_NAME)
                        .build());
        return mapToList(resultSet);
    }

    @Override
    public int update(Customer customer) throws SQLException {
        return statementExecutor.executeUpdate(new Query.Builder()
                .update(CUSTOMER_TABLE_NAME)
                .set(Map.of(
                        CUSTOMER_COLUMN_NAME, customer.name(),
                        CUSTOMER_COLUMN_NATIONAL_CODE, customer.nationalCode(),
                        CUSTOMER_COLUMN_PHONE, customer.phone()))
                .where(Condition.equalsTo(CUSTOMER_COLUMN_ID, customer.id()))
                .build());
    }

    @Override
    public int delete(int id) throws SQLException {
        return statementExecutor.executeUpdate(new Query.Builder()
                .deleteFrom(CUSTOMER_TABLE_NAME)
                .where(Condition.equalsTo(CUSTOMER_COLUMN_ID, id))
                .build());
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
