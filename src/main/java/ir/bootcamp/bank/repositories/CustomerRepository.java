package ir.bootcamp.bank.repositories;

import ir.bootcamp.bank.dbutil.Condition;
import ir.bootcamp.bank.dbutil.Query;
import ir.bootcamp.bank.dbutil.SingletonSessionFactory;
import ir.bootcamp.bank.model.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ir.bootcamp.bank.dbutil.DatabaseConstants.*;

public class CustomerRepository extends JdbcRepository<Customer> {

    private SessionFactory sessionFactory;

    public CustomerRepository(Connection connection) throws SQLException {
        super(connection);
        sessionFactory = SingletonSessionFactory.getSessionFactory();
    }

    @Override
    protected void createTable() throws SQLException {
    }

    @Override
    public int add(Customer customer) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(customer);
            session.getTransaction().commit();
            return customer.id();
        }
    }

    @Override
    public Customer find(int id) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Customer.class, id);
        }
    }

    public Customer find(String nationalCode) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            Customer customer = null;
            try {
                customer = session.createQuery("from Customer where nationalCode=:nationalCode", Customer.class)
                        .setParameter("nationalCode", nationalCode)
                        .getSingleResult();
            } catch (Exception ignored) {
            }
            return customer;
        }
    }

    @Override
    public List<Customer> findAll() throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            List<Customer> customers = new ArrayList<>();
            try {
                customers = session.createQuery("from Customer", Customer.class)
                        .getResultList();
            } catch (Exception ignored) {
            }
            return customers;
        }
    }

    @Override
    public int update(Customer customer) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(customer);
            session.getTransaction().commit();
            return customer.id();
        }
    }

    @Override
    public int delete(int id) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            return session.createQuery("delete from Customer where id=:id")
                    .setParameter("id", id)
                    .executeUpdate();
        }
    }

    @Override
    protected Customer mapTo(ResultSet resultSet) throws SQLException {
        if (!resultSet.next())
            return null;
        return new Customer(
                resultSet.getInt(CUSTOMER_COLUMN_ID),
                resultSet.getString(CUSTOMER_COLUMN_NAME),
                resultSet.getString(CUSTOMER_COLUMN_NATIONAL_CODE),
                resultSet.getString(CUSTOMER_COLUMN_PHONE)
        );
    }

    @Override
    protected List<Customer> mapToList(ResultSet resultSet) throws SQLException {
        List<Customer> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(new Customer(
                    resultSet.getInt(CUSTOMER_COLUMN_ID),
                    resultSet.getString(CUSTOMER_COLUMN_NAME),
                    resultSet.getString(CUSTOMER_COLUMN_NATIONAL_CODE),
                    resultSet.getString(CUSTOMER_COLUMN_PHONE)));
        }
        return result;
    }

}