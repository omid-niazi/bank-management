package ir.bootcamp.bank;

import ir.bootcamp.bank.repositories.*;
import ir.bootcamp.bank.service.*;
import ir.bootcamp.bank.util.PropertiesHelper;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.Scanner;

import static ir.bootcamp.bank.util.ConsoleMessageType.*;
import static ir.bootcamp.bank.util.ConsoleUtil.*;

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
            CardRepository cardRepository = new CardRepository(connection);
            AccountRepository accountRepository = new AccountRepository(connection);
            TransactionRepository transactionRepository = new TransactionRepository(connection);
            BranchManagerRepository branchManagerRepository = new BranchManagerRepository(connection);
            branchService = new BranchService(branchRepository);
            cardService = new CardService(cardRepository);
            accountService = new AccountService(accountRepository);
            transactionService = new TransactionService(transactionRepository);
            customerService = new CustomerService(customerRepository, cardService, accountService, transactionService);
            employeeService = new EmployeeService(employeeRepository, branchService,customerService, accountService );
            branchManagerService = new BranchManagerService(branchManagerRepository, branchService, employeeService);
            superUserService = new SuperUserService(branchService, branchManagerService);
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
            System.out.println("2- change branch manager");
            System.out.println("0- exit");
            int command = getCommandKey();
            scanner.nextLine();
            switch (command) {
                case 1:
                    createBranch();
                    break;
                case 2:
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
        System.out.println("enter branch id: ");
        int branchId = scanner.nextInt();
        System.out.println("enter manager id: ");
        int managerId = scanner.nextInt();
        superUserService.changeBranchManager(branchId, managerId);
    }

    private static void createBranch() throws SQLException {
        System.out.println("enter branch name: ");
        String name = scanner.nextLine();
        System.out.println("enter branch address: ");
        String address = scanner.nextLine();
        superUserService.createBranch(name, address);
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
