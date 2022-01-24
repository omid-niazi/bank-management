package ir.bootcamp.bank.service;

import ir.bootcamp.bank.exceptions.*;
import ir.bootcamp.bank.model.Account;
import ir.bootcamp.bank.model.Branch;
import ir.bootcamp.bank.model.Customer;
import ir.bootcamp.bank.model.Employee;
import ir.bootcamp.bank.repositories.EmployeeRepository;


import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class EmployeeService {
    private EmployeeRepository employeeRepository;
    private BranchService branchService;
    private CustomerService customerService;
    private AccountService accountService;
    private CardService cardService;
    private Employee loggedInEmployee;


    public EmployeeService(EmployeeRepository employeeRepository, BranchService branchService, CustomerService customerService, AccountService accountService, CardService cardService) {
        this.employeeRepository = employeeRepository;
        this.branchService = branchService;
        this.customerService = customerService;
        this.accountService = accountService;
        this.cardService = cardService;
    }

    public void login(String name, String password) throws SQLException, EmployeeNotFoundException, EmployeeInvalidPasswordException {
        loggedInEmployee = null;
        Employee employee = employeeRepository.findByName(name);
        if (employee == null) {
            throw new EmployeeNotFoundException("there is no employee with this username");
        }

        if (!employee.password().equals(password)) {
            throw new EmployeeInvalidPasswordException("wrong password");
        }

        loggedInEmployee = employee;
    }

    public void logout() {
        loggedInEmployee = null;
    }

    public void createEmployee(String name, String password) throws SQLException, EmployeeExistsException {
        createEmployee(name, password, loggedInEmployee, loggedInEmployee.branch());
    }

    public void createEmployee(String name, String password, String branchName) throws SQLException, BranchNotFoundException, EmployeeExistsException {
        Branch branch = branchService.find(branchName);
        if (branch == null) {
            throw new BranchNotFoundException("there is no branch with this name");
        }
        createEmployee(name, password, null, branch);
    }

    public void createEmployee(String name, String password, Employee manager, Branch branch) throws SQLException, EmployeeExistsException {
        if (employeeRepository.findByName(name) != null) {
            throw new EmployeeExistsException("this username is already taken");
        }
        Employee employee = new Employee(name, password, manager, branch);
        employeeRepository.add(employee);
    }

    public void createAccount(String accountNumber, long amount, String nationalCode) throws SQLException, CustomerNotFoundException, AccountExistsException {
        Customer customer = customerService.find(nationalCode);
        accountService.createAccount(accountNumber, amount, customer);
    }

    public void createAccount(String accountNumber, long amount, String name, String nationalCode, String phone) throws CustomerExistsException, SQLException, CustomerNotFoundException, AccountExistsException {
        customerService.createCustomer(name, nationalCode, phone);
        Customer customer = customerService.find(nationalCode);
        createAccount(accountNumber, amount, customer.nationalCode());
    }

    public void createCard(String cardNumber, short cvv2, Date expireDate, String accountNumber) throws SQLException, AccountNotFoundException, CardExistsException {
        Account account = accountService.find(accountNumber);
        cardService.createCard(cardNumber, cvv2, expireDate, account);
    }

    public List<Account> findCustomerAccounts(String nationalCode) throws SQLException, CustomerNotFoundException {
        Customer customer = customerService.find(nationalCode);
        if (customer == null) {
            throw new CustomerNotFoundException("there is no customer with this national code");
        }

        return accountService.findCustomerAccounts(customer);
    }

    public Employee find(int id) throws SQLException, EmployeeNotFoundException {
        Employee employee = employeeRepository.find(id);
        if (employee == null) {
            throw new EmployeeNotFoundException("there is no employee with this id");
        }
        return employee;
    }

    public Employee find(String username) throws SQLException, EmployeeNotFoundException {
        Employee employee = employeeRepository.findByName(username);
        if (employee == null) {
            throw new EmployeeNotFoundException("there is no employee with this username");
        }
        return employee;
    }


    public void removeAccount(String accountNumber) throws SQLException, AccountNotFoundException {
        accountService.removeAccount(accountNumber);
    }

    public void deposit(String accountNumber, long amount) throws SQLException, AccountNotFoundException {
        accountService.deposit(accountNumber, amount);
    }

    public void withdraw(String accountNumber, long amount) throws SQLException, AccountNotEnoughBalanceException, AccountNotFoundException {
        accountService.withdraw(accountNumber, amount);
    }

    public void removeCard(String cardNumber) throws SQLException, CardNotFoundException {
        cardService.remove(cardNumber);
    }
}
