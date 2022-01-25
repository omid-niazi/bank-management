package ir.bootcamp.bank.repositories;

import ir.bootcamp.bank.dbutil.Condition;
import ir.bootcamp.bank.dbutil.Query;
import ir.bootcamp.bank.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ir.bootcamp.bank.dbutil.DatabaseConstants.*;

public class TransactionRepository extends JdbcRepository<Transaction> {

    public TransactionRepository(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    protected void createTable() throws SQLException {
        String query = "create table if not exists " + TRANSACTION_TABLE_NAME + "" +
                "(" +
                "    " + TRANSACTION_COLUMN_ID + "       serial primary key," +
                "    " + TRANSACTION_COLUMN_FROM_CARD_ID + "     int not null ," +
                "    " + TRANSACTION_COLUMN_TO_CARD_ID + "     int ," +
                "    " + TRANSACTION_COLUMN_AMOUNT + "     bigint not null ," +
                "    " + TRANSACTION_COLUMN_IS_OUT_GOING + "     int not null, " +
                "    " + TRANSACTION_COLUMN_TIME + "     timestamp not null " +
                ");";
        connection.createStatement().execute(query);
    }

    @Override
    public int add(Transaction transaction) throws SQLException {
        ResultSet resultSet = statementExecutor.executeQuery(new Query.Builder()
                .insertInto(TRANSACTION_TABLE_NAME)
                .setValues(transaction.fromCard().id(), transaction.toCard().id(), transaction.amount(), transaction.isOutGoing() ? 1 : 0, transaction.timestamp())
                .returnColumns(TRANSACTION_COLUMN_ID)
                .build());
        if (resultSet.next()) {
            return resultSet.getInt(TRANSACTION_COLUMN_ID);
        }
        return -1;
    }

    @Override
    public Transaction find(int id) throws SQLException {
        ResultSet resultSet = statementExecutor.executeQuery(new Query.Builder()
                .select("tt.*",
                        "fc." + CARD_COLUMN_ID + " as fc_id",
                        "fc." + CARD_COLUMN_NUMBER + " as fc_number",
                        "tc." + CARD_COLUMN_ID + " as tc_id",
                        "tc." + CARD_COLUMN_NUMBER + " as tc_number")
                .from(TRANSACTION_TABLE_NAME + " tt")
                .innerJoin(CARD_TABLE_NAME + " fc")
                .on("tt." + TRANSACTION_COLUMN_FROM_CARD_ID + " = fc." + CARD_COLUMN_ID)
                .innerJoin(CARD_TABLE_NAME + " tc")
                .on("tt." + TRANSACTION_COLUMN_TO_CARD_ID + " = tc." + CARD_COLUMN_ID)
                .where(Condition.equalsTo(TRANSACTION_COLUMN_ID, id))
                .build());
        return mapTo(resultSet);
    }

    public List<Transaction> find(String cardNumber, Timestamp timestamp) throws SQLException {
        ResultSet resultSet =
                statementExecutor.executeQuery(new Query.Builder()
                        .select("tt.*",
                                "fc." + CARD_COLUMN_ID + " as fc_id",
                                "fc." + CARD_COLUMN_NUMBER + " as fc_number",
                                "tc." + CARD_COLUMN_ID + " as tc_id",
                                "tc." + CARD_COLUMN_NUMBER + " as tc_number")
                        .from(TRANSACTION_TABLE_NAME + " tt")
                        .innerJoin(CARD_TABLE_NAME + " fc")
                        .on("tt." + TRANSACTION_COLUMN_FROM_CARD_ID + " = fc." + CARD_COLUMN_ID)
                        .innerJoin(CARD_TABLE_NAME + " tc")
                        .on("tt." + TRANSACTION_COLUMN_TO_CARD_ID + " = tc." + CARD_COLUMN_ID)
                        .where(Condition.greaterThan("tt." + TRANSACTION_COLUMN_TIME, timestamp))
                        .and(Condition.equalsTo("fc." + CARD_COLUMN_NUMBER, cardNumber))
                        .or(Condition.greaterThan("tt." + TRANSACTION_COLUMN_TIME, timestamp))
                        .and(Condition.equalsTo("tc." + CARD_COLUMN_NUMBER, cardNumber))
                        .build());
        return mapToList(resultSet);
    }

    @Override
    public List<Transaction> findAll() throws SQLException {
        ResultSet resultSet =
                statementExecutor.executeQuery(new Query.Builder()
                        .select("tt.*",
                                "fc." + CARD_COLUMN_ID + " as fc_id",
                                "fc." + CARD_COLUMN_NUMBER + " as fc_number",
                                "tc." + CARD_COLUMN_ID + " as tc_id",
                                "tc." + CARD_COLUMN_NUMBER + " as tc_number")
                        .from(TRANSACTION_TABLE_NAME + " tt")
                        .innerJoin(CARD_TABLE_NAME + " fc")
                        .on("tt." + TRANSACTION_COLUMN_FROM_CARD_ID + " = fc." + CARD_COLUMN_ID)
                        .innerJoin(CARD_TABLE_NAME + " tc")
                        .on("tt." + TRANSACTION_COLUMN_TO_CARD_ID + " = tc." + CARD_COLUMN_ID)
                        .build());
        return mapToList(resultSet);
    }

    @Override
    public int update(Transaction transaction) throws SQLException {
        return statementExecutor.executeUpdate(new Query.Builder()
                .update(TRANSACTION_TABLE_NAME)
                .set(Map.of(
                        TRANSACTION_COLUMN_FROM_CARD_ID, transaction.fromCard().id(),
                        TRANSACTION_COLUMN_TO_CARD_ID, transaction.toCard().id(),
                        TRANSACTION_COLUMN_AMOUNT, transaction.amount(),
                        TRANSACTION_COLUMN_IS_OUT_GOING, transaction.isOutGoing() ? 1 : 0,
                        TRANSACTION_COLUMN_TIME, transaction.timestamp()))
                .where(Condition.equalsTo(TRANSACTION_COLUMN_ID, transaction.id()))
                .build());
    }

    @Override
    public int delete(int id) throws SQLException {
        return statementExecutor.executeUpdate(new Query.Builder()
                .deleteFrom(TRANSACTION_TABLE_NAME)
                .where(Condition.equalsTo(TRANSACTION_COLUMN_ID, id))
                .build());
    }

    @Override
    protected Transaction mapTo(ResultSet resultSet) throws SQLException {
        if (!resultSet.next())
            return null;
        Card fromCard = new Card(resultSet.getInt("fc_id"),
                resultSet.getString("fc_number"));

        Card toCard = new Card(resultSet.getInt("tc_id"),
                resultSet.getString("tc_number"));

        return new Transaction(resultSet.getInt(TRANSACTION_COLUMN_ID),
                fromCard, toCard
                , resultSet.getLong(TRANSACTION_COLUMN_AMOUNT),
                resultSet.getInt(TRANSACTION_COLUMN_IS_OUT_GOING) == 1, resultSet.getTimestamp(TRANSACTION_COLUMN_TIME));
    }

    @Override
    protected List<Transaction> mapToList(ResultSet resultSet) throws SQLException {
        List<Transaction> result = new ArrayList<>();
        while (resultSet.next()) {
            Card fromCard = new Card(resultSet.getInt("fc_id"),
                    resultSet.getString("fc_number"));

            Card toCard = new Card(resultSet.getInt("tc_id"),
                    resultSet.getString("tc_number"));


            result.add(new Transaction(resultSet.getInt(TRANSACTION_COLUMN_ID),
                    fromCard, toCard
                    , resultSet.getLong(TRANSACTION_COLUMN_AMOUNT),
                    resultSet.getInt(TRANSACTION_COLUMN_IS_OUT_GOING) == 1,
                    resultSet.getTimestamp(TRANSACTION_COLUMN_TIME)));
        }
        return result;
    }

}
