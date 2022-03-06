package ir.bootcamp.bank.repositories;

import ir.bootcamp.bank.dbutil.SingletonSessionFactory;
import ir.bootcamp.bank.model.Account;
import ir.bootcamp.bank.model.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ir.bootcamp.bank.dbutil.DatabaseConstants.*;

public class AccountRepository extends JdbcRepository<Account> {

    private SessionFactory sessionFactory;

    public AccountRepository(Connection connection) throws SQLException {
        super(connection);
        sessionFactory = SingletonSessionFactory.getSessionFactory();
    }

    @Override
    protected void createTable() throws SQLException {
    }

    @Override
    public int add(Account account) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(account);
            session.getTransaction().commit();
            return account.id();
        }
    }

    @Override
    public Account find(int id) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Account.class, id);
        }
    }

    public Account findByAccountNumber(String accountNumber) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            Account accountNumber1 = null;
            try {
                accountNumber1 = session.createQuery("from Account where accountNumber=:accountNumber", Account.class)
                        .setParameter("accountNumber", accountNumber).getSingleResult();
            } catch (Exception ignored) {
            }
            return accountNumber1;
        }
    }

    public List<Account> findByCustomerId(int customerId) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            List<Account> customerAccounts = new ArrayList<>();
            try {
                customerAccounts = session.createQuery("from Account where customer.id = :customerId", Account.class)
                        .setParameter("customerId", customerId)
                        .getResultList();
            } catch (Exception ignored) {
            }
            return customerAccounts;
        }
    }

    @Override
    public List<Account> findAll() throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            List<Account> accounts = new ArrayList<>();
            try {
                accounts = session.createQuery("from Account", Account.class).getResultList();
            } catch (Exception ignored) {
            }
            return accounts;
        }
    }

    @Override
    public int update(Account account) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(account);
            session.getTransaction().commit();
            return account.id();
        }
    }

    @Override
    public int delete(int id) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            int affectedRows = session.createQuery("delete from Account where id=:id")
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
            return affectedRows;
        }
    }

    @Override
    protected Account mapTo(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) return null;
        return new Account(resultSet.getInt(ACCOUNT_COLUMN_ID), resultSet.getString(ACCOUNT_COLUMN_NUMBER), resultSet.getLong(ACCOUNT_COLUMN_AMOUNT), new Customer(resultSet.getInt(CUSTOMER_COLUMN_ID), resultSet.getString(CUSTOMER_COLUMN_NAME), resultSet.getString(CUSTOMER_COLUMN_NATIONAL_CODE), resultSet.getString(CUSTOMER_COLUMN_PHONE)));
    }

    @Override
    protected List<Account> mapToList(ResultSet resultSet) throws SQLException {
        List<Account> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(new Account(resultSet.getInt(ACCOUNT_COLUMN_ID), resultSet.getString(ACCOUNT_COLUMN_NUMBER), resultSet.getLong(ACCOUNT_COLUMN_AMOUNT), new Customer(resultSet.getInt(CUSTOMER_COLUMN_ID), resultSet.getString(CUSTOMER_COLUMN_NAME), resultSet.getString(CUSTOMER_COLUMN_NATIONAL_CODE), resultSet.getString(CUSTOMER_COLUMN_PHONE))));
        }
        return result;
    }

    public void truncate() {
        String query = "delete from " + ACCOUNT_TABLE_NAME;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}