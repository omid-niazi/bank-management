package ir.bootcamp.bank.repositories;

import ir.bootcamp.bank.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ir.bootcamp.bank.repositories.Const.*;

public class TransactionRepository extends JdbcRepository<Transaction> {

    public TransactionRepository(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    protected void createTable() throws SQLException {
        String createTypeQuery = "CREATE TYPE transaction_type AS ENUM ('cash', 'transfer');";
        String query = "create table if not exists " + TRANSACTION_TABLE_NAME + "" +
                "(" +
                "    " + TRANSACTION_COLUMN_ID + "       serial primary key," +
                "    " + TRANSACTION_COLUMN_FROM_CARD_ID + "     int not null ," +
                "    " + TRANSACTION_COLUMN_TO_CARD_ID + "     int ," +
                "    " + TRANSACTION_COLUMN_AMOUNT + "     bigint not null ," +
                "    " + TRANSACTION_COLUMN_IS_OUT_GOING + "     int not null ," +
                "    " + TRANSACTION_COLUMN_TRANSACTION_TYPE + " transaction_type not null " +
                ");";
        connection.createStatement().execute(createTypeQuery);
        connection.createStatement().execute(query);
    }

    @Override
    public int add(Transaction transaction) throws SQLException {
        String sql = "insert into " + TRANSACTION_TABLE_NAME + " values (DEFAULT, ?, ?, ?, ?,?) returning " + TRANSACTION_COLUMN_ID + "";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, transaction.fromCard().id());
        if (transaction.toCard() != null)
            preparedStatement.setInt(2, transaction.toCard().id());
        preparedStatement.setLong(3, transaction.amount());
        preparedStatement.setInt(4, transaction.isOutGoing() ? 1 : 0);
        preparedStatement.setString(5, transaction.transactionType().name());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(TRANSACTION_COLUMN_ID);
        }
        return -1;
    }

    @Override
    public Transaction find(int id) throws SQLException {
        String sql = "select tt.*," +
                "fc." + CARD_COLUMN_ID + " as fc_id,  " +
                "fc." + CARD_COLUMN_NUMBER + " as fc_number,  " +
                "tc." + CARD_COLUMN_ID + " as tc_id,  " +
                "tc." + CARD_COLUMN_NUMBER + " as tc_number from " + TRANSACTION_TABLE_NAME +
                " tt inner join " + CARD_TABLE_NAME +
                " fc on tt." + TRANSACTION_COLUMN_FROM_CARD_ID + " = fc." + CARD_COLUMN_ID +
                " inner join " + CARD_TABLE_NAME +
                " tc on tt." + TRANSACTION_COLUMN_TO_CARD_ID + " = tc." + CARD_COLUMN_ID +
                " where " + TRANSACTION_COLUMN_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return mapTo(resultSet);
    }

    @Override
    public List<Transaction> findAll() throws SQLException {
        String sql = "select * from " + TRANSACTION_TABLE_NAME +
                " tt inner join " + CARD_TABLE_NAME +
                " fc on tt." + TRANSACTION_COLUMN_FROM_CARD_ID + " = fc." + CARD_COLUMN_ID +
                " inner join " + CARD_TABLE_NAME +
                " tc on tt." + TRANSACTION_COLUMN_TO_CARD_ID + " = tc." + CARD_COLUMN_ID;
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        return mapToList(resultSet);
    }

    @Override
    public int update(Transaction transaction) throws SQLException {
        String sql = "update " + TRANSACTION_TABLE_NAME + " set " +
                TRANSACTION_COLUMN_FROM_CARD_ID + " = ?, " +
                TRANSACTION_COLUMN_TO_CARD_ID + " = ?, " +
                TRANSACTION_COLUMN_AMOUNT + " = ?, " +
                TRANSACTION_COLUMN_IS_OUT_GOING + " = ?, " +
                TRANSACTION_COLUMN_TRANSACTION_TYPE + " = ? " +
                "where " + TRANSACTION_COLUMN_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, transaction.fromCard().id());
        if (transaction.toCard() != null)
            preparedStatement.setInt(2, transaction.toCard().id());
        preparedStatement.setLong(3, transaction.amount());
        preparedStatement.setInt(4, transaction.isOutGoing() ? 1 : 0);
        preparedStatement.setString(5, transaction.transactionType().name());
        preparedStatement.setInt(6, transaction.id());
        return preparedStatement.executeUpdate();
    }

    @Override
    public int delete(int id) throws SQLException {
        String sql = "delete from " + TRANSACTION_TABLE_NAME + " where " + TRANSACTION_COLUMN_ID + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeUpdate();
    }

    @Override
    protected Transaction mapTo(ResultSet resultSet) throws SQLException {
        if (!resultSet.next())
            return null;
        Card fromCard = new Card(resultSet.getInt("fc_id"),
                resultSet.getString("fc_number"));

        Card toCard = new Card(resultSet.getInt("tc_id"),
                resultSet.getString("tc_number"));

        if (resultSet.getString(TRANSACTION_COLUMN_TRANSACTION_TYPE).equals(TransactionType.cash.name())) {
            return new Transaction(resultSet.getInt(TRANSACTION_COLUMN_ID),
                    fromCard,
                    resultSet.getLong(TRANSACTION_COLUMN_AMOUNT),
                    resultSet.getInt(TRANSACTION_COLUMN_IS_OUT_GOING) == 1);
        } else {
            return new Transaction(resultSet.getInt(TRANSACTION_COLUMN_ID),
                    fromCard, toCard
                    , resultSet.getLong(TRANSACTION_COLUMN_AMOUNT),
                    resultSet.getInt(TRANSACTION_COLUMN_IS_OUT_GOING) == 1);
        }
    }

    @Override
    protected List<Transaction> mapToList(ResultSet resultSet) throws SQLException {
        List<Transaction> result = new ArrayList<>();
        while (resultSet.next()) {
            Card fromCard = new Card(resultSet.getInt("fc_id"),
                    resultSet.getString("fc_number"));

            Card toCard = new Card(resultSet.getInt("tc_id"),
                    resultSet.getString("tc_number"));

            if (resultSet.getString(TRANSACTION_COLUMN_TRANSACTION_TYPE).equals(TransactionType.cash.name())) {
                result.add(new Transaction(resultSet.getInt(TRANSACTION_COLUMN_ID),
                        fromCard,
                        resultSet.getLong(TRANSACTION_COLUMN_AMOUNT),
                        resultSet.getInt(TRANSACTION_COLUMN_IS_OUT_GOING) == 1));
            } else {
                result.add(new Transaction(resultSet.getInt(TRANSACTION_COLUMN_ID),
                        fromCard, toCard
                        , resultSet.getLong(TRANSACTION_COLUMN_AMOUNT),
                        resultSet.getInt(TRANSACTION_COLUMN_IS_OUT_GOING) == 1));
            }
        }
        return result;
    }

}
