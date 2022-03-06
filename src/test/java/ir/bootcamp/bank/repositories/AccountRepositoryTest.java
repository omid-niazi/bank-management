package ir.bootcamp.bank.repositories;

import ir.bootcamp.bank.dbutil.ConnectionFactory;
import ir.bootcamp.bank.model.Account;
import ir.bootcamp.bank.model.Customer;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountRepositoryTest {

    private static AccountRepository accountRepository;
    private static Account sampleAccount;
    private static Customer sampleCustomer;
    private static CustomerRepository customerRepository;

    @BeforeAll
    public static void beforeAll() throws SQLException, IOException {
        Connection connection = ConnectionFactory.getConnection("database-config.txt");
        sampleCustomer = new Customer("customer", "1920299017", "9384845050");
        customerRepository = new CustomerRepository(connection);
        int customerId = customerRepository.add(sampleCustomer);
        sampleCustomer = new Customer(customerId, sampleCustomer.name(), sampleCustomer.nationalCode(), sampleCustomer.phone());
        accountRepository = new AccountRepository(connection);
    }


    @BeforeEach
    void setUp() {
        sampleAccount = new Account("1234567890", 1000, sampleCustomer);
    }

    @AfterAll
    public static void afterAll() throws SQLException {
        customerRepository.delete(sampleCustomer.id());
    }


    @AfterEach
    void tearDown() {
        accountRepository.truncate();
    }

    @Test
    void add() throws SQLException {
        int insertId = accountRepository.add(sampleAccount);
        Account account = accountRepository.find(insertId);
        assertAll(
                () -> assertEquals(sampleAccount.accountNumber(), account.accountNumber()),
                () -> assertEquals(sampleAccount.amount(), account.amount()),
                () -> assertEquals(sampleAccount.customer().name(), account.customer().name()),
                () -> assertEquals(sampleAccount.customer().nationalCode(), account.customer().nationalCode()),
                () -> assertEquals(sampleAccount.customer().phone(), account.customer().phone())
        );
    }

    @Test
    void find() throws SQLException {
        int insertId = accountRepository.add(sampleAccount);
        Account account = accountRepository.find(insertId);
        assertAll(
                () -> assertEquals(sampleAccount.accountNumber(), account.accountNumber()),
                () -> assertEquals(sampleAccount.amount(), account.amount()),
                () -> assertEquals(sampleAccount.customer().name(), account.customer().name()),
                () -> assertEquals(sampleAccount.customer().nationalCode(), account.customer().nationalCode()),
                () -> assertEquals(sampleAccount.customer().phone(), account.customer().phone())
        );
    }

    @Test
    void findShouldReturnNull() throws SQLException {
        Account account = accountRepository.find(-1);
        assertNull(account);
    }


    @Test
    void findByAccountNumberShouldReturnNull() throws SQLException {
        Account account = accountRepository.findByAccountNumber("");
        assertNull(account);
    }

    @Test
    void findByAccountNumber() throws SQLException {
        accountRepository.add(sampleAccount);
        Account account = accountRepository.findByAccountNumber(sampleAccount.accountNumber());
        assertAll(
                () -> assertEquals(sampleAccount.accountNumber(), account.accountNumber()),
                () -> assertEquals(sampleAccount.amount(), account.amount()),
                () -> assertEquals(sampleAccount.customer().name(), account.customer().name()),
                () -> assertEquals(sampleAccount.customer().nationalCode(), account.customer().nationalCode()),
                () -> assertEquals(sampleAccount.customer().phone(), account.customer().phone())
        );
    }

    @Test
    void findByCustomerId() throws SQLException {
        Account firstAccount = sampleAccount;
        Account secondAccount = new Account("7894561234", 2000, sampleCustomer);
        int firstAccountId = accountRepository.add(firstAccount);
        int secondAccountId = accountRepository.add(secondAccount);

        firstAccount.setId(firstAccountId);
        secondAccount.setId(secondAccountId);

        List<Account> actualAccounts = accountRepository.findByCustomerId(sampleAccount.customer().id());
        List<Account> expectedAccounts = Arrays.asList(firstAccount, secondAccount);
        assertThat(actualAccounts)
                .hasSameElementsAs(expectedAccounts);
    }

    @Test
    void findByCustomerIdShouldReturnNull() throws SQLException {
        List<Account> accounts = accountRepository.findByCustomerId(-1);
        assertEquals(0, accounts.size());
    }

    @Test
    void findAll() throws SQLException {
        Account firstAccount = sampleAccount;
        Account secondAccount = new Account("7894561234", 2000, sampleCustomer);
        int firstAccountId = accountRepository.add(firstAccount);
        int secondAccountId = accountRepository.add(secondAccount);

        firstAccount.setId(firstAccountId);
        secondAccount.setId(secondAccountId);


        List<Account> actualAccounts = accountRepository.findAll();
        List<Account> expectedAccounts = Arrays.asList(firstAccount, secondAccount);
        assertThat(actualAccounts).hasSameElementsAs(expectedAccounts);
    }

    @Test
    void findAllShouldReturnNull() throws SQLException {
        List<Account> accounts = accountRepository.findAll();

        assertThat(accounts.size()).isEqualTo(0);
    }

    @Test
    void update() throws SQLException {
        int insertId = accountRepository.add(sampleAccount);
        Account waitForUpdateAccount = new Account(insertId, "4444444444", 5000, sampleCustomer);
        accountRepository.update(waitForUpdateAccount);

        Account updatedAccount = accountRepository.findAll().get(0);

        assertAll(
                () -> assertEquals(waitForUpdateAccount.accountNumber(), updatedAccount.accountNumber()),
                () -> assertEquals(waitForUpdateAccount.amount(), updatedAccount.amount()),
                () -> assertEquals(waitForUpdateAccount.customer().name(), updatedAccount.customer().name()),
                () -> assertEquals(waitForUpdateAccount.customer().nationalCode(), updatedAccount.customer().nationalCode()),
                () -> assertEquals(waitForUpdateAccount.customer().phone(), updatedAccount.customer().phone())
        );
    }

    @Test
    void delete() throws SQLException {
        int insertedId = accountRepository.add(sampleAccount);
        accountRepository.delete(insertedId);
        Account account = accountRepository.find(insertedId);
        assertNull(account);
    }

}