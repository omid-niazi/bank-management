package ir.bootcamp.bank.repositories;

import ir.bootcamp.bank.dbutil.Condition;
import ir.bootcamp.bank.dbutil.Query;
import ir.bootcamp.bank.model.Account;
import ir.bootcamp.bank.model.Card;
import ir.bootcamp.bank.model.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ir.bootcamp.bank.dbutil.DatabaseConstants.*;

public class CardRepository extends JdbcRepository<Card> {

    public CardRepository(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    protected void createTable() throws SQLException {
        String query = "create table if not exists " + CARD_TABLE_NAME + "" +
                "(" +
                "    " + CARD_COLUMN_ID + "       serial primary key," +
                "    " + CARD_COLUMN_NUMBER + "     char(16) not null ," +
                "    " + CARD_COLUMN_CVV2 + " smallint not null ," +
                "    " + CARD_COLUMN_PASSWORD + " varchar(255) not null ," +
                "    " + CARD_COLUMN_EXPIRE_DATE + " date not null ," +
                "    " + CARD_COLUMN_ACCOUNT_ID + " int not null ," +
                "    " + CARD_COLUMN_FAILED_ATTEMPT + " int not null ," +
                "    " + CARD_COLUMN_STATUS + " int not null, " +
                "foreign key (" + CARD_COLUMN_ACCOUNT_ID + ") references " + ACCOUNT_TABLE_NAME + "(" + ACCOUNT_COLUMN_ID + ") " +
                ");";
        connection.createStatement().execute(query);
    }

    @Override
    public int add(Card card) throws SQLException {
        ResultSet resultSet = statementExecutor.executeQuery(new Query.Builder()
                .insertInto(CARD_TABLE_NAME)
                .setValues(card.cardNumber(), card.cvv2(), card.password(), card.expireDate(), card.account().id(), card.failedAttempt(), card.status())
                .returnColumns(CARD_COLUMN_ID)
                .build());
        if (resultSet.next()) {
            return resultSet.getInt(CARD_COLUMN_ID);
        }
        return -1;
    }

    @Override
    public Card find(int id) throws SQLException {
        ResultSet resultSet =
                statementExecutor.executeQuery(new Query.Builder()
                        .select("*")
                        .from(CARD_TABLE_NAME)
                        .innerJoin(ACCOUNT_TABLE_NAME)
                        .on(CARD_TABLE_NAME + "." + CARD_COLUMN_ACCOUNT_ID + " = " + ACCOUNT_TABLE_NAME + "." + ACCOUNT_COLUMN_ID)
                        .innerJoin(CUSTOMER_TABLE_NAME)
                        .on(ACCOUNT_TABLE_NAME + "." + ACCOUNT_COLUMN_CUSTOMER_ID + " = " + CUSTOMER_TABLE_NAME + "." + CUSTOMER_COLUMN_ID)
                        .where(Condition.equalsTo(CARD_COLUMN_ID, id))
                        .build());
        return mapTo(resultSet);
    }


    public Card find(String cardNumber) throws SQLException {
        ResultSet resultSet =
                statementExecutor.executeQuery(new Query.Builder()
                        .select("*")
                        .from(CARD_TABLE_NAME)
                        .innerJoin(ACCOUNT_TABLE_NAME)
                        .on(CARD_TABLE_NAME + "." + CARD_COLUMN_ACCOUNT_ID + " = " + ACCOUNT_TABLE_NAME + "." + ACCOUNT_COLUMN_ID)
                        .innerJoin(CUSTOMER_TABLE_NAME)
                        .on(ACCOUNT_TABLE_NAME + "." + ACCOUNT_COLUMN_CUSTOMER_ID + " = " + CUSTOMER_TABLE_NAME + "." + CUSTOMER_COLUMN_ID)
                        .where(Condition.equalsTo(CARD_COLUMN_NUMBER, cardNumber))
                        .build());
        return mapTo(resultSet);
    }

    @Override
    public List<Card> findAll() throws SQLException {
        ResultSet resultSet =
                statementExecutor.executeQuery(new Query.Builder()
                        .select("*")
                        .from(CARD_TABLE_NAME)
                        .innerJoin(ACCOUNT_TABLE_NAME)
                        .on(CARD_TABLE_NAME + "." + CARD_COLUMN_ACCOUNT_ID + " = " + ACCOUNT_TABLE_NAME + "." + ACCOUNT_COLUMN_ID)
                        .innerJoin(CUSTOMER_TABLE_NAME)
                        .on(ACCOUNT_TABLE_NAME + "." + ACCOUNT_COLUMN_CUSTOMER_ID + " = " + CUSTOMER_TABLE_NAME + "." + CUSTOMER_COLUMN_ID)
                        .build());
        return mapToList(resultSet);
    }

    @Override
    public int update(Card card) throws SQLException {
        return statementExecutor.executeUpdate(new Query.Builder()
                .update(CARD_TABLE_NAME)
                .set(Map.of(
                        CARD_COLUMN_NUMBER, card.cardNumber(),
                        CARD_COLUMN_CVV2, card.cvv2(),
                        CARD_COLUMN_PASSWORD, card.password(),
                        CARD_COLUMN_EXPIRE_DATE, card.expireDate(),
                        CARD_COLUMN_ACCOUNT_ID, card.account().id(),
                        CARD_COLUMN_FAILED_ATTEMPT, card.failedAttempt(),
                        CARD_COLUMN_STATUS, card.status()))
                .where(Condition.equalsTo(CARD_COLUMN_ID, card.id()))
                .build());
    }

    @Override
    public int delete(int id) throws SQLException {
        return statementExecutor.executeUpdate(new Query.Builder()
                .deleteFrom(CARD_TABLE_NAME)
                .where(Condition.equalsTo(CARD_COLUMN_ID, id))
                .build());
    }

    @Override
    protected Card mapTo(ResultSet resultSet) throws SQLException {
        if (!resultSet.next())
            return null;
        return new Card(resultSet.getInt(CARD_COLUMN_ID),
                resultSet.getString(CARD_COLUMN_NUMBER),
                resultSet.getShort(CARD_COLUMN_CVV2),
                resultSet.getDate(CARD_COLUMN_EXPIRE_DATE),
                resultSet.getString(CARD_COLUMN_PASSWORD),
                new Account(
                        resultSet.getInt(ACCOUNT_COLUMN_ID),
                        resultSet.getString(ACCOUNT_COLUMN_NUMBER),
                        resultSet.getLong(ACCOUNT_COLUMN_AMOUNT),
                        new Customer(
                                resultSet.getInt(CUSTOMER_COLUMN_ID),
                                resultSet.getString(CUSTOMER_COLUMN_NAME),
                                resultSet.getString(CUSTOMER_COLUMN_NATIONAL_CODE),
                                resultSet.getString(CUSTOMER_COLUMN_PHONE)
                        )),
                resultSet.getInt(CARD_COLUMN_FAILED_ATTEMPT),
                resultSet.getByte(CARD_COLUMN_STATUS)
        );
    }

    @Override
    protected List<Card> mapToList(ResultSet resultSet) throws SQLException {
        List<Card> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(new Card(resultSet.getInt(CARD_COLUMN_ID),
                    resultSet.getString(CARD_COLUMN_NUMBER),
                    resultSet.getShort(CARD_COLUMN_CVV2),
                    resultSet.getDate(CARD_COLUMN_EXPIRE_DATE),
                    resultSet.getString(CARD_COLUMN_PASSWORD),
                    new Account(
                            resultSet.getInt(ACCOUNT_COLUMN_ID),
                            resultSet.getString(ACCOUNT_COLUMN_NUMBER),
                            resultSet.getLong(ACCOUNT_COLUMN_AMOUNT),
                            new Customer(
                                    resultSet.getInt(CUSTOMER_COLUMN_ID),
                                    resultSet.getString(CUSTOMER_COLUMN_NAME),
                                    resultSet.getString(CUSTOMER_COLUMN_NATIONAL_CODE),
                                    resultSet.getString(CUSTOMER_COLUMN_PHONE)
                            )),
                    resultSet.getInt(CARD_COLUMN_FAILED_ATTEMPT),
                    resultSet.getByte(CARD_COLUMN_STATUS)
            ));
        }
        return result;
    }

}
