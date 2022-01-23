package ir.bootcamp.bank.service;

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

    public void createBranch(String name, String address) throws SQLException {
        branchService.createBranch(name, address);
    }

    public void changeBranchManager(int branchId, int managerId) throws SQLException {
        branchManagerService.changeBranchManager(branchId, managerId);
    }

    public void createEmployee(String name, String passwod, String branchName) throws SQLException {
        employeeService.createEmployee(name, passwod, branchName);
    }
}
