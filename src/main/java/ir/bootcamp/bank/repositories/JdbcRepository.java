package ir.bootcamp.bank.repositories;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class JdbcRepository<T> implements Repository<T> {

    protected Connection connection;

    public JdbcRepository(Connection connection) throws SQLException {
        this.connection = connection;
        createTable();
    }

    protected abstract T mapTo(ResultSet resultSet) throws SQLException;

    protected abstract List<T> mapToList(ResultSet resultSet) throws SQLException;

    protected abstract void createTable() throws SQLException;
}
