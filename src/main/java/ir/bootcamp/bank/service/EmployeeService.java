package ir.bootcamp.bank.service;

import ir.bootcamp.bank.model.Account;
import ir.bootcamp.bank.model.Branch;
import ir.bootcamp.bank.model.Customer;
import ir.bootcamp.bank.model.Employee;
import ir.bootcamp.bank.repositories.EmployeeRepository;

import static ir.bootcamp.bank.util.ConsoleUtil.*;
import static ir.bootcamp.bank.util.ConsoleMessageType.*;

import java.sql.SQLException;

public class EmployeeService {
    private EmployeeRepository employeeRepository;
    private BranchService branchService;
    private CustomerService customerService;
    private AccountService accountService;
    private Employee loggedInEmployee;

    public EmployeeService(EmployeeRepository employeeRepository, BranchService branchService, CustomerService customerService, AccountService accountService) {
        this.employeeRepository = employeeRepository;
        this.branchService = branchService;
        this.customerService = customerService;
        this.accountService = accountService;
    }

    public boolean login(String name, String password) throws SQLException {
        loggedInEmployee = null;
        Employee employee = employeeRepository.findByName(name);
        if (employee == null) {
            print("employee doesn't exists", error);
            return false;
        }

        if (!employee.password().equals(password)) {
            print("password is wrong", error);
            return false;
        }

        loggedInEmployee = employee;
        print("logged in successfully", success);
        return true;
    }


    public void createEmployee(String name, String password) throws SQLException {
        createEmployee(name, password, loggedInEmployee, loggedInEmployee.branch());
    }

    public void createEmployee(String name, String password, String branchName) throws SQLException {
        Branch branch = branchService.find(branchName);
        if (branch == null) {
            print("branch name is not correct", error);
            return;
        }
        createEmployee(name, password, null, branch);
    }

    public void createEmployee(String name, String password, Employee manager, Branch branch) throws SQLException {
        if (employeeRepository.findByName(name) != null) {
            print("name is already taken", error);
            return;
        }
        Employee employee = new Employee(name, password, manager, branch);
        employeeRepository.add(employee);
        print("employee record created", success);
    }

    public void showCustomerAccounts(int customerId) throws SQLException {
        Customer customer = customerService.find(customerId);
        if (customer == null) {
            print("customer id is wrong", error);
            return;
        }

        for (Account customerAccount : accountService.findCustomerAccounts(customer)) {
            print(customerAccount.toString(), info);
        }

    }

    public Employee find(int id) throws SQLException {
        return employeeRepository.find(id);
    }


}
