package ir.bootcamp.bank.service;

import ir.bootcamp.bank.exceptions.*;

import java.sql.SQLException;

public class SuperUserService {
    private BranchService branchService;
    private EmployeeService employeeService;
    private BranchManagerService branchManagerService;

    public SuperUserService(BranchService branchService, EmployeeService employeeService, BranchManagerService branchManagerService) {
        this.branchService = branchService;
        this.employeeService = employeeService;
        this.branchManagerService = branchManagerService;
    }

    public void createBranch(String name, String address) throws SQLException, BranchExistsException {
        branchService.createBranch(name, address);
    }

    public void changeBranchManager(String branchname, String username) throws SQLException, BranchNotFoundException, IllegalBranchManagerException, EmployeeNotFoundException {
        branchManagerService.changeBranchManager(branchname, username);
    }

    public void createEmployee(String name, String passwod, String branchName) throws SQLException, BranchNotFoundException, EmployeeExistsException {
        employeeService.createEmployee(name, passwod, branchName);
    }
}
