package ir.bootcamp.bank.repositories;

import ir.bootcamp.bank.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ir.bootcamp.bank.repositories.Const.*;

public class BranchManagerRepository extends JdbcRepository<BranchManager> {

    public BranchManagerRepository(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    protected void createTable() throws SQLException {
        String query = "create table if not exists " + BRANCH_MANAGER_TABLE_NAME + "" +
                "(" +
                "    " + BRANCH_MANAGER_COLUMN_ID + "       serial primary key," +
                "    " + BRANCH_MANAGER_COLUMN_BRANCH_ID + "     int not null ," +
                "    " + BRANCH_MANAGER_COLUMN_MANAGER_ID + " int unique not null " +
                ");";
        connection.createStatement().execute(query);
    }

    @Override
    public int add(BranchManager branchManager) throws SQLException {
        String sql = "insert into " + BRANCH_MANAGER_TABLE_NAME + " values (DEFAULT, ?, ?) returning " + BRANCH_MANAGER_COLUMN_ID + "";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, branchManager.branch().id());
        preparedStatement.setInt(2, branchManager.manager().id());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(BRANCH_MANAGER_COLUMN_ID);
        }
        return -1;
    }

    @Override
    public BranchManager find(int id) throws SQLException {
        String sql = "select * from " + BRANCH_MANAGER_TABLE_NAME +
                " inner join " + BRANCH_TABLE_NAME +
                " on " + BRANCH_MANAGER_TABLE_NAME + "." + BRANCH_MANAGER_COLUMN_BRANCH_ID + " = " + BRANCH_TABLE_NAME + "." + BRANCH_COLUMN_ID +
                " inner join " + EMPLOYEE_TABLE_NAME +
                " on " + BRANCH_MANAGER_TABLE_NAME + "." + BRANCH_MANAGER_COLUMN_MANAGER_ID + " = " + EMPLOYEE_TABLE_NAME + "." + EMPLOYEE_COLUMN_ID +
                " where " + BRANCH_MANAGER_COLUMN_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    public BranchManager findByBranchId(int id) throws SQLException {
        String sql = "select * from " + BRANCH_MANAGER_TABLE_NAME +
                " inner join " + BRANCH_TABLE_NAME +
                " on " + BRANCH_MANAGER_TABLE_NAME + "." + BRANCH_MANAGER_COLUMN_BRANCH_ID + " = " + BRANCH_TABLE_NAME + "." + BRANCH_COLUMN_ID +
                " inner join " + EMPLOYEE_TABLE_NAME +
                " on " + BRANCH_MANAGER_TABLE_NAME + "." + BRANCH_MANAGER_COLUMN_MANAGER_ID + " = " + EMPLOYEE_TABLE_NAME + "." + EMPLOYEE_COLUMN_ID +
                " where " + BRANCH_MANAGER_COLUMN_BRANCH_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    public BranchManager findByBranchName(String branchName) throws SQLException {
        String sql = "select * from " + BRANCH_MANAGER_TABLE_NAME +
                " inner join " + BRANCH_TABLE_NAME +
                " on " + BRANCH_MANAGER_TABLE_NAME + "." + BRANCH_MANAGER_COLUMN_BRANCH_ID + " = " + BRANCH_TABLE_NAME + "." + BRANCH_COLUMN_ID +
                " inner join " + EMPLOYEE_TABLE_NAME +
                " on " + BRANCH_MANAGER_TABLE_NAME + "." + BRANCH_MANAGER_COLUMN_MANAGER_ID + " = " + EMPLOYEE_TABLE_NAME + "." + EMPLOYEE_COLUMN_ID +
                " where " + BRANCH_TABLE_NAME + "." + BRANCH_COLUMN_NAME + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, branchName);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    @Override
    public List<BranchManager> findAll() throws SQLException {
        String sql = "select * from " + BRANCH_MANAGER_TABLE_NAME +
                " inner join " + BRANCH_TABLE_NAME +
                " on " + BRANCH_MANAGER_TABLE_NAME + "." + BRANCH_MANAGER_COLUMN_BRANCH_ID + " = " + BRANCH_TABLE_NAME + "." + BRANCH_COLUMN_ID +
                " inner join " + EMPLOYEE_TABLE_NAME +
                " on " + BRANCH_MANAGER_TABLE_NAME + "." + BRANCH_MANAGER_COLUMN_MANAGER_ID + " = " + EMPLOYEE_TABLE_NAME + "." + EMPLOYEE_COLUMN_ID;
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        return mapToList(resultSet);
    }

    @Override
    public int update(BranchManager branchManager) throws SQLException {
        String sql = "update " + BRANCH_MANAGER_TABLE_NAME + " set " +
                BRANCH_MANAGER_COLUMN_BRANCH_ID + " = ?, " +
                BRANCH_MANAGER_COLUMN_MANAGER_ID + " = ? " +
                "where " + BRANCH_MANAGER_COLUMN_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, branchManager.branch().id());
        preparedStatement.setInt(2, branchManager.manager().id());
        return preparedStatement.executeUpdate();
    }

    @Override
    public int delete(int id) throws SQLException {
        String sql = "delete from " + BRANCH_MANAGER_TABLE_NAME + " where " + BRANCH_MANAGER_COLUMN_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeUpdate();
    }

    @Override
    protected BranchManager mapTo(ResultSet resultSet) throws SQLException {
        if (!resultSet.next())
            return null;
        return new BranchManager(resultSet.getInt(BRANCH_MANAGER_COLUMN_ID),
                new Branch(resultSet.getInt(BRANCH_COLUMN_ID),
                        resultSet.getString(BRANCH_COLUMN_NAME),
                        resultSet.getString(BRANCH_COLUMN_ADDRESS)),
                new Employee(
                        resultSet.getInt(EMPLOYEE_COLUMN_ID),
                        resultSet.getString(EMPLOYEE_COLUMN_NAME),
                        resultSet.getString(EMPLOYEE_COLUMN_PASSWORD),
                        null,
                        null));
    }

    @Override
    protected List<BranchManager> mapToList(ResultSet resultSet) throws SQLException {
        List<BranchManager> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(new BranchManager(resultSet.getInt(BRANCH_MANAGER_COLUMN_ID),
                    new Branch(resultSet.getInt(BRANCH_COLUMN_ID),
                            resultSet.getString(BRANCH_COLUMN_NAME),
                            resultSet.getString(BRANCH_COLUMN_ADDRESS)),
                    new Employee(
                            resultSet.getInt(EMPLOYEE_COLUMN_ID),
                            resultSet.getString(EMPLOYEE_COLUMN_NAME),
                            resultSet.getString(EMPLOYEE_COLUMN_PASSWORD),
                            null,
                            null)));
        }
        return result;
    }

}
