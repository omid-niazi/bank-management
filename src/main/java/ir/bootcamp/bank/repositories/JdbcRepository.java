package ir.bootcamp.bank.repositories;

import ir.bootcamp.bank.dbutil.PreparedStatementExecutor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class JdbcRepository<T> implements Repository<T> {

    protected Connection connection;
    protected PreparedStatementExecutor statementExecutor;

    public JdbcRepository(Connection connection) throws SQLException {
        this.connection = connection;
        this.statementExecutor = new PreparedStatementExecutor(connection);
        createTable();
    }

    protected abstract T mapTo(ResultSet resultSet) throws SQLException;

    protected abstract List<T> mapToList(ResultSet resultSet) throws SQLException;

    protected abstract void createTable() throws SQLException;
}
