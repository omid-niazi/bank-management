package ir.bootcamp.bank.repositories;

import ir.bootcamp.bank.dbutil.Condition;
import ir.bootcamp.bank.dbutil.Query;
import ir.bootcamp.bank.model.Branch;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ir.bootcamp.bank.dbutil.DatabaseConstants.*;

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
        ResultSet resultSet = statementExecutor.executeQuery(new Query.Builder()
                .insertInto(BRANCH_TABLE_NAME)
                .setValues(branch.name(), branch.address())
                .returnColumns(BRANCH_COLUMN_ID)
                .build());
        if (resultSet.next()) {
            return resultSet.getInt(BRANCH_COLUMN_ID);
        }
        return -1;
    }

    @Override
    public Branch find(int id) throws SQLException {
        ResultSet resultSet =
                statementExecutor.executeQuery(new Query.Builder()
                        .select("*")
                        .from(BRANCH_TABLE_NAME)
                        .where(Condition.equalsTo(BRANCH_COLUMN_ID, id))
                        .build());
        return mapTo(resultSet);
    }

    public Branch find(String branchName) throws SQLException {
        ResultSet resultSet =
                statementExecutor.executeQuery(new Query.Builder()
                        .select("*")
                        .from(BRANCH_TABLE_NAME)
                        .where(Condition.equalsTo(BRANCH_COLUMN_NAME, branchName))
                        .build());
        return mapTo(resultSet);
    }

    @Override
    public List<Branch> findAll() throws SQLException {
        ResultSet resultSet =
                statementExecutor.executeQuery(new Query.Builder()
                        .select("*")
                        .from(BRANCH_TABLE_NAME)
                        .build());
        return mapToList(resultSet);
    }

    @Override
    public int update(Branch branch) throws SQLException {
        return statementExecutor.executeUpdate(new Query.Builder()
                .update(BRANCH_TABLE_NAME)
                .set(Map.of(
                        BRANCH_TABLE_NAME, branch.name(),
                        BRANCH_COLUMN_ADDRESS, branch.address()))
                .where(Condition.equalsTo(BRANCH_COLUMN_ID, branch.id()))
                .build());
    }

    @Override
    public int delete(int id) throws SQLException {
        return statementExecutor.executeUpdate(new Query.Builder()
                .deleteFrom(BRANCH_TABLE_NAME)
                .where(Condition.equalsTo(BRANCH_COLUMN_ID, id))
                .build());
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
