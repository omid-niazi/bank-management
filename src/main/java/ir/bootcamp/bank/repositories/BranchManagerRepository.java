package ir.bootcamp.bank.repositories;

import ir.bootcamp.bank.dbutil.Condition;
import ir.bootcamp.bank.dbutil.Query;
import ir.bootcamp.bank.model.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ir.bootcamp.bank.dbutil.DatabaseConstants.*;

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
        ResultSet resultSet = statementExecutor.executeQuery(new Query.Builder()
                .insertInto(BRANCH_MANAGER_TABLE_NAME)
                .setValues(branchManager.branch().id(), branchManager.manager().id())
                .returnColumns(BRANCH_MANAGER_COLUMN_ID)
                .build());
        if (resultSet.next()) {
            return resultSet.getInt(BRANCH_MANAGER_COLUMN_ID);
        }
        return -1;
    }

    @Override
    public BranchManager find(int id) throws SQLException {
        ResultSet resultSet =
                statementExecutor.executeQuery(new Query.Builder()
                        .select("*")
                        .from(BRANCH_MANAGER_TABLE_NAME)
                        .innerJoin(BRANCH_TABLE_NAME)
                        .on(BRANCH_MANAGER_TABLE_NAME + "." + BRANCH_MANAGER_COLUMN_BRANCH_ID + " = " + BRANCH_TABLE_NAME + "." + BRANCH_COLUMN_ID)
                        .innerJoin(EMPLOYEE_TABLE_NAME)
                        .on(BRANCH_MANAGER_TABLE_NAME + "." + BRANCH_MANAGER_COLUMN_MANAGER_ID + " = " + EMPLOYEE_TABLE_NAME + "." + EMPLOYEE_COLUMN_ID)
                        .where(Condition.equalsTo(BRANCH_MANAGER_COLUMN_ID, id))
                        .build());
        return mapTo(resultSet);
    }

    public BranchManager findByBranchId(int id) throws SQLException {
        ResultSet resultSet =
                statementExecutor.executeQuery(new Query.Builder()
                        .select("*")
                        .from(BRANCH_MANAGER_TABLE_NAME)
                        .innerJoin(BRANCH_TABLE_NAME)
                        .on(BRANCH_MANAGER_TABLE_NAME + "." + BRANCH_MANAGER_COLUMN_BRANCH_ID + " = " + BRANCH_TABLE_NAME + "." + BRANCH_COLUMN_ID)
                        .innerJoin(EMPLOYEE_TABLE_NAME)
                        .on(BRANCH_MANAGER_TABLE_NAME + "." + BRANCH_MANAGER_COLUMN_MANAGER_ID + " = " + EMPLOYEE_TABLE_NAME + "." + EMPLOYEE_COLUMN_ID)
                        .where(Condition.equalsTo(BRANCH_MANAGER_COLUMN_BRANCH_ID, id))
                        .build());
        return mapTo(resultSet);
    }

    public BranchManager findByBranchName(String branchName) throws SQLException {
        ResultSet resultSet =
                statementExecutor.executeQuery(new Query.Builder()
                        .select("*")
                        .from(BRANCH_MANAGER_TABLE_NAME)
                        .innerJoin(BRANCH_TABLE_NAME)
                        .on(BRANCH_MANAGER_TABLE_NAME + "." + BRANCH_MANAGER_COLUMN_BRANCH_ID + " = " + BRANCH_TABLE_NAME + "." + BRANCH_COLUMN_ID)
                        .innerJoin(EMPLOYEE_TABLE_NAME)
                        .on(BRANCH_MANAGER_TABLE_NAME + "." + BRANCH_MANAGER_COLUMN_MANAGER_ID + " = " + EMPLOYEE_TABLE_NAME + "." + EMPLOYEE_COLUMN_ID)
                        .where(Condition.equalsTo(BRANCH_TABLE_NAME + "." + BRANCH_COLUMN_NAME, branchName))
                        .build());
        return mapTo(resultSet);
    }

    @Override
    public List<BranchManager> findAll() throws SQLException {
        ResultSet resultSet =
                statementExecutor.executeQuery(new Query.Builder()
                        .select("*")
                        .from(BRANCH_MANAGER_TABLE_NAME)
                        .innerJoin(BRANCH_TABLE_NAME)
                        .on(BRANCH_MANAGER_TABLE_NAME + "." + BRANCH_MANAGER_COLUMN_BRANCH_ID + " = " + BRANCH_TABLE_NAME + "." + BRANCH_COLUMN_ID)
                        .innerJoin(EMPLOYEE_TABLE_NAME)
                        .on(BRANCH_MANAGER_TABLE_NAME + "." + BRANCH_MANAGER_COLUMN_MANAGER_ID + " = " + EMPLOYEE_TABLE_NAME + "." + EMPLOYEE_COLUMN_ID)
                        .build());
        return mapToList(resultSet);
    }

    @Override
    public int update(BranchManager branchManager) throws SQLException {
        return statementExecutor.executeUpdate(new Query.Builder()
                .update(BRANCH_MANAGER_TABLE_NAME)
                .set(Map.of(
                        BRANCH_MANAGER_COLUMN_BRANCH_ID, branchManager.branch().id(),
                        BRANCH_MANAGER_COLUMN_MANAGER_ID, branchManager.manager().id()))
                .where(Condition.equalsTo(BRANCH_MANAGER_COLUMN_ID, branchManager.id()))
                .build());
    }

    @Override
    public int delete(int id) throws SQLException {
        return statementExecutor.executeUpdate(new Query.Builder()
                .deleteFrom(BRANCH_MANAGER_TABLE_NAME)
                .where(Condition.equalsTo(BRANCH_MANAGER_COLUMN_ID, id))
                .build());
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
