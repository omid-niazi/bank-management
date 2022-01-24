package ir.bootcamp.bank.service;

import ir.bootcamp.bank.exceptions.BranchNotFoundException;
import ir.bootcamp.bank.exceptions.EmployeeNotFoundException;
import ir.bootcamp.bank.exceptions.IllegalBranchManagerException;
import ir.bootcamp.bank.model.Branch;
import ir.bootcamp.bank.model.BranchManager;
import ir.bootcamp.bank.model.Employee;
import ir.bootcamp.bank.repositories.BranchManagerRepository;

import java.sql.SQLException;

public class BranchManagerService {
    private BranchManagerRepository branchManagerRepository;
    private BranchService branchService;
    private EmployeeService employeeService;

    public BranchManagerService(BranchManagerRepository branchManagerRepository, BranchService branchService, EmployeeService employeeService) {
        this.branchManagerRepository = branchManagerRepository;
        this.branchService = branchService;
        this.employeeService = employeeService;
    }

    public void changeBranchManager(String branchName, String managerName) throws SQLException, EmployeeNotFoundException, IllegalBranchManagerException, BranchNotFoundException {
        Branch branch = branchService.find(branchName);

        Employee employee = employeeService.find(managerName);

        if (!employee.branch().name().equals(branchName)) {
            throw new IllegalBranchManagerException("this employee doesn't work at this branch");
        }

        BranchManager branchManager = branchManagerRepository.findByBranchName(branchName);
        if (branchManager != null) {
            branchManagerRepository.update(new BranchManager(
                    branchManager.id(),
                    branchManager.branch(),
                    employee
            ));
        } else {
            branchManagerRepository.add(new BranchManager(
                    branch,
                    employee
            ));
        }
    }
}
