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

    public void login(String name, String password) throws SQLException {
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

    public void createEmployee(String name, String password) throws SQLException {
        createEmployee(name, password, loggedInEmployee, loggedInEmployee.branch());
    }

    public void createEmployee(String name, String password, String branchName) throws SQLException {
        Branch branch = branchService.find(branchName);
        if (branch == null) {
            throw new BranchNotFoundException("there is no branch with this name");
        }
        createEmployee(name, password, null, branch);
    }

    public void createEmployee(String name, String password, Employee manager, Branch branch) throws SQLException {
        if (employeeRepository.findByName(name) != null) {
            throw new EmployeeExistsException("this username is already taken");
        }
        Employee employee = new Employee(name, password, manager, branch);
        employeeRepository.add(employee);
    }

    public void createAccount(String accountNumber, long amount, String nationalCode) throws SQLException {
        Customer customer = customerService.find(nationalCode);
        accountService.createAccount(accountNumber, amount, customer);
    }

    public void createAccount(String accountNumber, long amount, String name, String nationalCode, String phone) throws CustomerExistsException, SQLException {
        customerService.createCustomer(name, nationalCode, phone);
        Customer customer = customerService.find(nationalCode);
        createAccount(accountNumber, amount, customer.nationalCode());
    }

    public void createCard(String cardNumber, short cvv2, Date expireDate, String accountNumber) throws SQLException {
        Account account = accountService.find(accountNumber);
        cardService.createCard(cardNumber, cvv2, expireDate, account);
    }

    public List<Account> findCustomerAccounts(int customerId) throws SQLException {
        Customer customer = customerService.find(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("there is no customer with this id");
        }

        return accountService.findCustomerAccounts(customer);
    }

    public Employee find(int id) throws SQLException {
        return employeeRepository.find(id);
    }


}
