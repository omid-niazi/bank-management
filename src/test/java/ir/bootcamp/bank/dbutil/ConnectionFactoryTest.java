package ir.bootcamp.bank.dbutil;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionFactoryTest {


    @Test
    void getConnectionWithWrongFilePath() {
        assertThrows(NullPointerException.class, () -> ConnectionFactory.getConnection("wrongpath"));
    }

    @Test
    void getConnectionWithCorrectPath() {
        assertAll(
                () -> assertDoesNotThrow(() -> ConnectionFactory.getConnection("database-config.txt")),
                () -> assertNotNull(ConnectionFactory.getConnection("database-config.txt"))
        );
    }
}