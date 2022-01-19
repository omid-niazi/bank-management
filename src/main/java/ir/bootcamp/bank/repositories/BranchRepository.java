package ir.bootcamp.bank.repositories;

import ir.bootcamp.bank.model.Branch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ir.bootcamp.bank.repositories.Const.*;

public class BranchRepository extends JdbcRepository<Branch> {


    public BranchRepository(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    protected void createTable() throws SQLException {
        String query = "create table if not exists " + BRANCH_TABLE_NAME + "" +
                "(" +
                "    " + BRANCH_COLUMN_ID + "       serial primary key," +
                "    " + BRANCH_COLUMN_NAME + "     varchar(255) not null ," +
                "    " + BRANCH_COLUMN_ADDRESS + " varchar(255) unique not null " +
                ");";
        connection.createStatement().execute(query);
    }

    @Override
    public int add(Branch branch) throws SQLException {
        String sql = "insert into " + BRANCH_TABLE_NAME + " values (DEFAULT, ?, ?) returning " + BRANCH_COLUMN_ID + "";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, branch.name());
        preparedStatement.setString(2, branch.address());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(CUSTOMER_COLUMN_ID);
        }
        return -1;
    }

    @Override
    public Branch find(int id) throws SQLException {
        String sql = "select * from " + BRANCH_TABLE_NAME + " where " + BRANCH_COLUMN_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    public Branch find(String branchName) throws SQLException {
        String sql = "select * from " + BRANCH_TABLE_NAME + " where " + BRANCH_COLUMN_NAME + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, branchName);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    @Override
    public List<Branch> findAll() throws SQLException {
        String sql = "select * from " + BRANCH_TABLE_NAME + "";
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        return mapToList(resultSet);
    }

    @Override
    public int update(Branch branch) throws SQLException {
        String sql = "update " + BRANCH_TABLE_NAME + " set " + BRANCH_TABLE_NAME + " = ?, " + BRANCH_COLUMN_ADDRESS + " = ? where " + BRANCH_COLUMN_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, branch.name());
        preparedStatement.setString(2, branch.address());
        preparedStatement.setInt(3, branch.id());
        return preparedStatement.executeUpdate();
    }

    @Override
    public int delete(int id) throws SQLException {
        String sql = "delete from " + BRANCH_TABLE_NAME + " where " + BRANCH_COLUMN_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeUpdate();
    }

    @Override
    protected Branch mapTo(ResultSet resultSet) throws SQLException {
        if (!resultSet.next())
            return null;
        return new Branch(resultSet.getInt(BRANCH_COLUMN_ID),
                resultSet.getString(BRANCH_COLUMN_NAME),
                resultSet.getString(BRANCH_COLUMN_ADDRESS));
    }

    @Override
    protected List<Branch> mapToList(ResultSet resultSet) throws SQLException {
        List<Branch> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(new Branch(resultSet.getInt(BRANCH_COLUMN_ID),
                    resultSet.getString(BRANCH_COLUMN_NAME),
                    resultSet.getString(BRANCH_COLUMN_ADDRESS)));
        }
        return result;
    }

}
