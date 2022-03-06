package ir.bootcamp.bank.dbutil;

import ir.bootcamp.bank.model.Account;
import ir.bootcamp.bank.model.Customer;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class SingletonSessionFactory {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            var registry = new StandardServiceRegistryBuilder()
                    .configure() // goes and fetches configuration from hibernate.cfg.xml
                    .build();
            sessionFactory = new MetadataSources(registry)
                    .addAnnotatedClass(Account.class)
                    .addAnnotatedClass(Customer.class)
                    .buildMetadata()
                    .buildSessionFactory();
        }

        return sessionFactory;
    }
}
