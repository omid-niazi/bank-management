package ir.bootcamp.bank.dbutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PreparedStatementExecutor {

    private Connection connection;

    public PreparedStatementExecutor(Connection connection) {
        this.connection = connection;
    }

    public ResultSet executeQuery(Query query) throws SQLException {
        return buildPreparedStatement(query).executeQuery();
    }

    public boolean execute(Query query) throws SQLException {
        return buildPreparedStatement(query).execute();
    }

    public int executeUpdate(Query query) throws SQLException {
        return buildPreparedStatement(query).executeUpdate();
    }

    public PreparedStatement buildPreparedStatement(Query query) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query.getQuery());
        List<Object> values = query.getValues();
        for (int i = 0; i < values.size(); i++) {
            preparedStatement.setObject(i + 1, values.get(i));
        }
        return preparedStatement;
    }

}
