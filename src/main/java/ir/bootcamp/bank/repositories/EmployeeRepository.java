package ir.bootcamp.bank.repositories;

import ir.bootcamp.bank.dbutil.Condition;
import ir.bootcamp.bank.dbutil.Query;
import ir.bootcamp.bank.model.Branch;
import ir.bootcamp.bank.model.Employee;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ir.bootcamp.bank.dbutil.DatabaseConstants.*;

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
        ResultSet resultSet = statementExecutor.executeQuery(new Query.Builder()
                .insertInto(EMPLOYEE_TABLE_NAME)
                .setValues(employee.name(), employee.password(), employee.directManager() != null ? employee.directManager().id() : Integer.MIN_VALUE, employee.branch().id())
                .returnColumns(EMPLOYEE_COLUMN_ID)
                .build());
        if (resultSet.next()) {
            return resultSet.getInt(EMPLOYEE_COLUMN_ID);
        }
        return -1;
    }

    @Override
    public Employee find(int id) throws SQLException {
        ResultSet resultSet = statementExecutor.executeQuery(new Query.Builder()
                .select("emp.*",
                        "br.*",
                        "mgr." + EMPLOYEE_COLUMN_ID + " as mgr_id",
                        "mgr." + EMPLOYEE_COLUMN_NAME + " as mgr_name",
                        "mgr." + EMPLOYEE_COLUMN_PASSWORD + " as mgr_password")
                .from(EMPLOYEE_TABLE_NAME + " emp")
                .innerJoin(BRANCH_TABLE_NAME + " br")
                .on("br." + BRANCH_COLUMN_ID + " =  emp." + EMPLOYEE_COLUMN_BRANCH_ID)
                .leftJoin(EMPLOYEE_TABLE_NAME + " mgr")
                .on("emp." + EMPLOYEE_COLUMN_MANAGER_ID + " = mgr." + EMPLOYEE_COLUMN_ID)
                .where(Condition.equalsTo("emp." + EMPLOYEE_COLUMN_ID, id))
                .build());
        return mapTo(resultSet);
    }

    public Employee findByName(String name) throws SQLException {
        ResultSet resultSet = statementExecutor.executeQuery(new Query.Builder()
                .select("emp.*",
                        "br.*",
                        "mgr." + EMPLOYEE_COLUMN_ID + " as mgr_id",
                        "mgr." + EMPLOYEE_COLUMN_NAME + " as mgr_name",
                        "mgr." + EMPLOYEE_COLUMN_PASSWORD + " as mgr_password")
                .from(EMPLOYEE_TABLE_NAME + " emp")
                .innerJoin(BRANCH_TABLE_NAME + " br")
                .on("br." + BRANCH_COLUMN_ID + " =  emp." + EMPLOYEE_COLUMN_BRANCH_ID)
                .leftJoin(EMPLOYEE_TABLE_NAME + " mgr")
                .on("emp." + EMPLOYEE_COLUMN_MANAGER_ID + " = mgr." + EMPLOYEE_COLUMN_ID)
                .where(Condition.equalsTo("emp." + EMPLOYEE_COLUMN_NAME, name))
                .build());
        return mapTo(resultSet);
    }

    @Override
    public List<Employee> findAll() throws SQLException {
        ResultSet resultSet = statementExecutor.executeQuery(new Query.Builder()
                .select("emp.*",
                        "br.*",
                        "mgr." + EMPLOYEE_COLUMN_ID + " as mgr_id",
                        "mgr." + EMPLOYEE_COLUMN_NAME + " as mgr_name",
                        "mgr." + EMPLOYEE_COLUMN_PASSWORD + " as mgr_password")
                .from(EMPLOYEE_TABLE_NAME + " emp")
                .innerJoin(BRANCH_TABLE_NAME + " br")
                .on("br." + BRANCH_COLUMN_ID + " =  emp." + EMPLOYEE_COLUMN_BRANCH_ID)
                .leftJoin(EMPLOYEE_TABLE_NAME + " mgr")
                .on("emp." + EMPLOYEE_COLUMN_MANAGER_ID + " = mgr." + EMPLOYEE_COLUMN_ID)
                .build());
        return mapToList(resultSet);
    }

    @Override
    public int update(Employee employee) throws SQLException {
        return statementExecutor.executeUpdate(new Query.Builder()
                .update(EMPLOYEE_TABLE_NAME)
                .set(Map.of(
                        EMPLOYEE_COLUMN_NAME, employee.name(),
                        EMPLOYEE_COLUMN_PASSWORD, employee.password(),
                        EMPLOYEE_COLUMN_MANAGER_ID, employee.directManager().id(),
                        EMPLOYEE_COLUMN_BRANCH_ID, employee.branch().id()))
                .where(Condition.equalsTo(CARD_COLUMN_ACCOUNT_ID, employee.id()))
                .build());
    }

    @Override
    public int delete(int id) throws SQLException {
        return statementExecutor.executeUpdate(new Query.Builder()
                .deleteFrom(EMPLOYEE_TABLE_NAME)
                .where(Condition.equalsTo(EMPLOYEE_COLUMN_ID, id))
                .build());
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
