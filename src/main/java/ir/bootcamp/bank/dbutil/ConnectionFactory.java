package ir.bootcamp.bank.dbutil;

import ir.bootcamp.bank.util.PropertiesHelper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private static Connection connection;

    public static Connection getConnection(String connectionConfigPath) throws SQLException, IOException {
        if (connection == null) {
            Properties properties = getProperties(connectionConfigPath);
            connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));
        }
        return connection;
    }

    private static Properties getProperties(String connectionConfigPath) throws IOException {
        return PropertiesHelper.loadPropertiesFile(connectionConfigPath);
    }
}
