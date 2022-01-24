package ir.bootcamp.bank.repositories;

import ir.bootcamp.bank.model.Branch;
import ir.bootcamp.bank.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ir.bootcamp.bank.util.DatabaseConstants.*;

public class EmployeeRepository extends JdbcRepository<Employee> {


    public EmployeeRepository(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    protected void createTable() throws SQLException {
        String query = "create table if not exists " + EMPLOYEE_TABLE_NAME + "" + "("
                + "    " + EMPLOYEE_COLUMN_ID + "       serial primary key,"
                + "    " + EMPLOYEE_COLUMN_NAME + "     varchar(255) not null unique ,"
                + "    " + EMPLOYEE_COLUMN_PASSWORD + " varchar(255) not null ,"
                + "    " + EMPLOYEE_COLUMN_MANAGER_ID + " int ,"
                + "    " + EMPLOYEE_COLUMN_BRANCH_ID + " int "
                + ");";
        connection.createStatement().execute(query);
    }

    @Override
    public int add(Employee employee) throws SQLException {
        String sql = "insert into " + EMPLOYEE_TABLE_NAME +
                " values (DEFAULT, ?, ?, ?, ?) " +
                "returning " + EMPLOYEE_COLUMN_ID + "";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, employee.name());
        preparedStatement.setString(2, employee.password());
        if (employee.directManager() != null)
            preparedStatement.setInt(3, employee.directManager().id());
        else
            preparedStatement.setInt(3, Integer.MIN_VALUE);
        preparedStatement.setInt(4, employee.branch().id());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(EMPLOYEE_COLUMN_ID);
        }
        return -1;
    }

    @Override
    public Employee find(int id) throws SQLException {
        String sql = "select " +
                "emp.*, " +
                "br.*, " +
                "mgr." + EMPLOYEE_COLUMN_ID + " as mgr_id, " +
                "mgr." + EMPLOYEE_COLUMN_NAME + " as mgr_name, " +
                "mgr." + EMPLOYEE_COLUMN_PASSWORD + " as mgr_password from " + EMPLOYEE_TABLE_NAME +
                " emp inner join " + BRANCH_TABLE_NAME + " br on " +
                "br." + BRANCH_COLUMN_ID + " =  emp." + EMPLOYEE_COLUMN_BRANCH_ID +
                " left join " + EMPLOYEE_TABLE_NAME + " mgr on emp." + EMPLOYEE_COLUMN_MANAGER_ID + " = mgr." + EMPLOYEE_COLUMN_ID +
                " where " + EMPLOYEE_COLUMN_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    public Employee findByName(String name) throws SQLException {
        String sql = "select " +
                "emp.*, " +
                "br.*, " +
                "mgr." + EMPLOYEE_COLUMN_ID + " as mgr_id, " +
                "mgr." + EMPLOYEE_COLUMN_NAME + " as mgr_name, " +
                "mgr." + EMPLOYEE_COLUMN_PASSWORD + " as mgr_password from " + EMPLOYEE_TABLE_NAME +
                " emp inner join " + BRANCH_TABLE_NAME + " br on " +
                "br." + BRANCH_COLUMN_ID + " =  emp." + EMPLOYEE_COLUMN_BRANCH_ID +
                " left join " + EMPLOYEE_TABLE_NAME + " mgr on emp." + EMPLOYEE_COLUMN_MANAGER_ID + " = mgr." + EMPLOYEE_COLUMN_ID +
                " where emp." + EMPLOYEE_COLUMN_NAME + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    @Override
    public List<Employee> findAll() throws SQLException {
        String sql = "select " +
                "emp.*, " +
                "br.*, " +
                "mgr." + EMPLOYEE_COLUMN_ID + " as mgr_id, " +
                "mgr." + EMPLOYEE_COLUMN_NAME + " as mgr_name, " +
                "mgr." + EMPLOYEE_COLUMN_PASSWORD + " as mgr_password from " + EMPLOYEE_TABLE_NAME +
                " emp inner join " + BRANCH_TABLE_NAME + " br on " +
                "br." + BRANCH_COLUMN_ID + " =  emp." + EMPLOYEE_COLUMN_BRANCH_ID +
                " left join " + EMPLOYEE_TABLE_NAME + " mgr on emp." + EMPLOYEE_COLUMN_MANAGER_ID + " = mgr." + EMPLOYEE_COLUMN_ID;
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        return mapToList(resultSet);
    }

    @Override
    public int update(Employee employee) throws SQLException {
        String sql = "update " + EMPLOYEE_TABLE_NAME + " set " + EMPLOYEE_COLUMN_NAME + " = ?, " + EMPLOYEE_COLUMN_PASSWORD + " = ?, " + EMPLOYEE_COLUMN_MANAGER_ID + " = ?, " + EMPLOYEE_COLUMN_BRANCH_ID + " = ? where " + EMPLOYEE_COLUMN_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, employee.name());
        preparedStatement.setString(2, employee.password());
        preparedStatement.setInt(3, employee.directManager().id());
        preparedStatement.setInt(4, employee.branch().id());
        preparedStatement.setInt(5, employee.id());
        return preparedStatement.executeUpdate();
    }

    @Override
    public int delete(int id) throws SQLException {
        String sql = "delete from " + EMPLOYEE_TABLE_NAME + " where " + EMPLOYEE_COLUMN_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeUpdate();
    }

    @Override
    protected Employee mapTo(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) return null;
        return new Employee(
                resultSet.getInt(EMPLOYEE_COLUMN_ID),
                resultSet.getString(EMPLOYEE_COLUMN_NAME),
                resultSet.getString(EMPLOYEE_COLUMN_PASSWORD),
                new Employee(resultSet.getInt("mgr_id"),
                        resultSet.getString("mgr_name"),
                        resultSet.getString("mgr_password"),
                        null,
                        null),
                new Branch(resultSet.getInt(BRANCH_COLUMN_ID),
                        resultSet.getString(BRANCH_COLUMN_NAME),
                        resultSet.getString(BRANCH_COLUMN_ADDRESS))
        );
    }

    @Override
    protected List<Employee> mapToList(ResultSet resultSet) throws SQLException {
        List<Employee> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(new Employee(
                    resultSet.getInt(EMPLOYEE_COLUMN_ID),
                    resultSet.getString(EMPLOYEE_COLUMN_NAME),
                    resultSet.getString(EMPLOYEE_COLUMN_PASSWORD),
                    new Employee(resultSet.getInt("mgr_id"),
                            resultSet.getString("mgr_name"),
                            resultSet.getString("mgr_password"),
                            null,
                            null),
                    new Branch(resultSet.getInt(BRANCH_COLUMN_ID),
                            resultSet.getString(BRANCH_COLUMN_NAME),
                            resultSet.getString(BRANCH_COLUMN_ADDRESS))
            ));
        }
        return result;
    }

}
