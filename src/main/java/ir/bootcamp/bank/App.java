package ir.bootcamp.bank;

import ir.bootcamp.bank.exceptions.*;
import ir.bootcamp.bank.model.Account;
import ir.bootcamp.bank.model.Transaction;
import ir.bootcamp.bank.repositories.*;
import ir.bootcamp.bank.service.*;
import ir.bootcamp.bank.util.PropertiesHelper;

import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import static ir.bootcamp.bank.util.ConsoleMessageType.*;
import static ir.bootcamp.bank.util.ConsoleUtil.print;

public class App {
    private static Scanner scanner;
    private static AccountService accountService;
    private static BranchManagerService branchManagerService;
    private static BranchService branchService;
    private static CardService cardService;
    private static CustomerService customerService;
    private static EmployeeService employeeService;
    private static SuperUserService superUserService;
    private static TransactionService transactionService;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);


        Properties properties = null;
        try {
            properties = PropertiesHelper.loadPropertiesFile("database-config.txt");
        } catch (IOException e) {
            print("create database-config.txt in resources folder", error);
            return;
        }

        try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));) {

            EmployeeRepository employeeRepository = new EmployeeRepository(connection);
            BranchRepository branchRepository = new BranchRepository(connection);
            CustomerRepository customerRepository = new CustomerRepository(connection);
            AccountRepository accountRepository = new AccountRepository(connection);
            CardRepository cardRepository = new CardRepository(connection);
            TransactionRepository transactionRepository = new TransactionRepository(connection);
            BranchManagerRepository branchManagerRepository = new BranchManagerRepository(connection);
            branchService = new BranchService(branchRepository);
            cardService = new CardService(cardRepository);
            accountService = new AccountService(accountRepository);
            transactionService = new TransactionService(transactionRepository);
            customerService = new CustomerService(customerRepository, cardService, accountService, transactionService);
            employeeService = new EmployeeService(employeeRepository, branchService, customerService, accountService, cardService, transactionService);
            branchManagerService = new BranchManagerService(branchManagerRepository, branchService, employeeService);
            superUserService = new SuperUserService(branchService, employeeService, branchManagerService);
            mainMenu();
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("password") || e.getMessage().contains("user")) {
                System.out.println("please check the database-config.txt file in resources folder");
                return;
            }
            throw new RuntimeException();
        }
    }

    private static void mainMenu() throws SQLException {
        while (true) {
            System.out.println("1- super user menu");
            System.out.println("2- employee login");
            System.out.println("3- transfer money");
            System.out.println("4- change card password");
            System.out.println("0- exit");
            int command = getCommandKey();
            scanner.nextLine();
            switch (command) {
                case 1:
                    superUserMenu();
                    break;
                case 2:
                    employeeLogin();
                    break;
                case 3:
                    transfer();
                    break;
                case 4:
                    changeCardPassword();
                    break;
                case 0:
                    return;
                default:
                    print("wrong command", error);
            }
        }
    }

    private static void superUserMenu() throws SQLException {
        while (true) {
            System.out.println("1- create branch");
            System.out.println("2- create employee");
            System.out.println("3- change branch manager");
            System.out.println("0- exit");
            int command = getCommandKey();
            scanner.nextLine();
            switch (command) {
                case 1:
                    createBranch();
                    break;
                case 2:
                    createEmployeeBySuperUser();
                    break;
                case 3:
                    changeBranchManager();
                    break;
                case 0:
                    return;
                default:
                    print("wrong command", error);
            }
        }
    }

    private static void changeBranchManager() throws SQLException {
        System.out.println("enter branch name: ");
        String branchName = scanner.nextLine();
        System.out.println("enter manager username: ");
        String managerName = scanner.nextLine();
        try {
            superUserService.changeBranchManager(branchName, managerName);
            print("branch created", success);
        } catch (BranchNotFoundException | IllegalBranchManagerException | EmployeeNotFoundException e) {
            print(e.getMessage(), error);
        }
    }

    private static void createEmployeeBySuperUser() throws SQLException {
        System.out.println("enter username: ");
        String username = scanner.nextLine();
        System.out.println("enter password: ");
        String password = scanner.nextLine();
        System.out.println("enter branch name: ");
        String branchName = scanner.nextLine();
        try {
            superUserService.createEmployee(username, password, branchName);
            print("employee added", success);
        } catch (BranchNotFoundException | EmployeeExistsException e) {
            print(e.getMessage(), error);
        }
    }

    private static void createBranch() throws SQLException {
        System.out.println("enter branch name: ");
        String name = scanner.nextLine();
        System.out.println("enter branch address: ");
        String address = scanner.nextLine();
        try {
            superUserService.createBranch(name, address);
            print("branch created", success);
        } catch (BranchExistsException e) {
            print(e.getMessage(), error);
        }
    }

    private static void employeeLogin() throws SQLException {
        System.out.println("enter username: ");
        String username = scanner.nextLine();
        System.out.println("enter password: ");
        String password = scanner.nextLine();
        try {
            employeeService.login(username, password);
            print("you logged in successfully", success);
            showEmployeeMenu();
        } catch (EmployeeNotFoundException | EmployeeInvalidPasswordException e) {
            print(e.getMessage(), error);
        }
    }

    private static void showEmployeeMenu() throws SQLException {
        while (true) {
            System.out.println("1- create employee");
            System.out.println("2- create account");
            System.out.println("3- remove account");
            System.out.println("4- deposit");
            System.out.println("5- withdraw");
            System.out.println("6- create card");
            System.out.println("7- remove card");
            System.out.println("8- show customer accounts");
            System.out.println("9- show transactions");
            System.out.println("10- enable card");
            System.out.println("11- disable card");
            System.out.println("0- exit");
            int command = getCommandKey();
            scanner.nextLine();
            switch (command) {
                case 1:
                    createEmployee();
                    break;
                case 2:
                    createAccount();
                    break;
                case 3:
                    removeAccount();
                    break;
                case 4:
                    deposit();
                    break;
                case 5:
                    withdraw();
                    break;
                case 6:
                    createCard();
                    break;
                case 7:
                    removeCard();
                    break;
                case 8:
                    showCustomerAccounts();
                    break;
                case 9:
                    showTransactions();
                    break;
                case 10:
                    changeCardStatus(1);
                    break;
                case 11:
                    changeCardStatus(0);
                    break;
                case 0:
                    return;
                default:
                    print("wrong command", error);
            }
        }
    }

    private static void createAccount() throws SQLException {
        System.out.println("1- create for existed customer");
        System.out.println("2- create account for new customer");
        System.out.println("0- exist");
        int command = getCommandKey();
        scanner.nextLine();
        switch (command) {
            case 1:
                createAccountForExistedCustomer();
                break;
            case 2:
                createAccountForNewCustomer();
                break;
            case 0:
                return;
            default:
                print("wrong command", error);
        }
    }

    private static void removeAccount() throws SQLException {
        System.out.println("enter account number");
        String accountNumber = scanner.nextLine();
        try {
            employeeService.removeAccount(accountNumber);
            print("account removed" , success);
        } catch (AccountNotFoundException e) {
            print(e.getMessage(), error);
        }
    }

    private static void deposit() throws SQLException {
        System.out.println("enter account number");
        String accountNumber = scanner.nextLine();
        System.out.println("enter amount");
        long amount = scanner.nextLong();
        scanner.nextLine();
        try {
            employeeService.deposit(accountNumber, amount);
            print("account balance increased", success);
        } catch (AccountNotFoundException e) {
            print(e.getMessage(), error);
        }
    }

    private static void withdraw() throws SQLException {
        System.out.println("enter account number");
        String accountNumber = scanner.nextLine();
        System.out.println("enter amount");
        long amount = scanner.nextLong();
        scanner.nextLine();
        try {
            employeeService.withdraw(accountNumber, amount);
            print("account balance decreased", success);
        } catch (AccountNotEnoughBalanceException | AccountNotFoundException e) {
            print(e.getMessage(), error);
        }
    }

    private static void createCard() throws SQLException {
        System.out.println("enter card number: ");
        String cardNumber = scanner.nextLine();
        System.out.println("enter cvv2: ");
        short cvv2 = scanner.nextShort();
        scanner.nextLine();
        System.out.println("enter expire date (format 2022-01-01): ");
        String expireDateInString = scanner.nextLine();
        Date expireDate = Date.valueOf(expireDateInString);
        System.out.println("enter account number");
        String acountNumber = scanner.nextLine();
        try {
            employeeService.createCard(cardNumber, cvv2, expireDate, acountNumber);
            print("card created", success);
        } catch (AccountNotFoundException | CardExistsException e) {
            print(e.getMessage(), error);
        }
    }

    private static void removeCard() throws SQLException {
        System.out.println("enter card number");
        String cardNumber = scanner.nextLine();
        try {
            employeeService.removeCard(cardNumber);
            print("card removed", success);
        } catch (CardNotFoundException e) {
            print(e.getMessage(), error);
        }
    }

    private static void showCustomerAccounts() throws SQLException {
        System.out.println("enter customer national code ");
        String nationalCode = scanner.nextLine();
        try {
            List<Account> customerAccounts = employeeService.findCustomerAccounts(nationalCode);
            for (Account customerAccount : customerAccounts) {
                print(customerAccount.toString(), info);
            }
        } catch (CustomerNotFoundException e) {
            print(e.getMessage(), error);
        }
    }

    private static void showTransactions() throws SQLException {
        System.out.println("enter card number");
        String cardNumber = scanner.nextLine();
        System.out.println("enter time(yyy-[m]m-[d]d hh:mm:ss format): ");
        Timestamp timestamp = Timestamp.valueOf(scanner.nextLine());
        List<Transaction> transactions = employeeService.findTransactions(cardNumber, timestamp);
        for (Transaction transaction : transactions) {
            print(transaction.toString(), info);
        }
    }

    private static void createAccountForExistedCustomer() throws SQLException {
        System.out.println("enter account number");
        String accountNumber = scanner.nextLine();
        System.out.println("enter amount");
        long amount = scanner.nextLong();
        scanner.nextLine();
        System.out.println("enter customer national code");
        String nationalCode = scanner.nextLine();
        try {
            employeeService.createAccount(accountNumber, amount, nationalCode);
            print("account created", success);
        } catch (CustomerNotFoundException | AccountExistsException e) {
            print(e.getMessage(), error);
        }
    }

    private static void createAccountForNewCustomer() throws SQLException {
        System.out.println("enter account number");
        String accountNumber = scanner.nextLine();
        System.out.println("enter amount");
        long amount = scanner.nextLong();
        scanner.nextLine();
        System.out.println("enter customer name");
        String name = scanner.nextLine();
        System.out.println("enter customer national code");
        String nationalCode = scanner.nextLine();
        System.out.println("enter customer phone");
        String phone = scanner.nextLine();
        try {
            employeeService.createAccount(accountNumber, amount, name, nationalCode, phone);
            print("account created", success);
        } catch (CustomerExistsException | CustomerNotFoundException | AccountExistsException e) {
            print(e.getMessage(), error);
        }
    }

    private static void createEmployee() throws SQLException {
        System.out.println("enter username: ");
        String username = scanner.nextLine();
        System.out.println("enter password: ");
        String password = scanner.nextLine();
        try {
            employeeService.createEmployee(username, password);
            print("employee created", success);
        } catch (EmployeeExistsException e) {
            print(e.getMessage(), error);
        }
    }

    private static void transfer() throws SQLException {
        System.out.println("enter your card number: ");
        String fromCard = scanner.nextLine();
        System.out.println("enter the card which you want transfer money to: ");
        String toCard = scanner.nextLine();
        System.out.println("enter amount: ");
        long amount = scanner.nextLong();
        scanner.nextLine();
        try {
            customerService.cardValidation(fromCard, toCard, amount);
        } catch (InvalidCardNumberException | CardNotFoundException | AccountNotEnoughBalanceException e) {
            print(e.getMessage(), error);
            return;
        }

        System.out.println("enter your cvv2: ");
        short cvv2 = scanner.nextShort();
        scanner.nextLine();
        System.out.println("enter expire date (format 2022-01-01): ");
        String expireDateInString = scanner.nextLine();
        Date expireDate = Date.valueOf(expireDateInString);
        System.out.println("enter your password");
        String password = scanner.nextLine();
        try {
            customerService.transfer(fromCard, password, cvv2, expireDate, amount, toCard);
            print("transaction completed", success);
        } catch (AccountNotEnoughBalanceException | CardAuthenticationException | CardNotFoundException | AccountNotFoundException | CardDisabledException e) {
            print(e.getMessage(), error);
        }
    }

    private static void changeCardPassword() throws SQLException {
        System.out.println("enter card number: ");
        String cardNumber = scanner.nextLine();
        System.out.println("enter your password (press enter key if you didn't set yet): ");
        String oldPassword = scanner.nextLine();
        System.out.println("enter your new password");
        String newPassword = scanner.nextLine();
        try {
            customerService.changePassword(cardNumber, oldPassword, newPassword);
            print("your password changed", success);
        } catch (CardNotFoundException | InvalidCardPasswordException e) {
            print(e.getMessage(), error);
        }
    }

    private static void changeCardStatus(int status) throws SQLException {
        System.out.println("enter card number");
        String cardNumber = scanner.nextLine();
        try {
            employeeService.changeCardStatus(cardNumber, status);
            print("card status changed", success);
        } catch (CardNotFoundException e) {
            print(e.getMessage(), error);
        }
    }

    private static int getCommandKey() {
        int key = -1;
        try {
            key = scanner.nextInt();
        } catch (Exception ignored) {

        }
        return key;
    }
}
