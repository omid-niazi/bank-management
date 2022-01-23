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

    public void changeBranchManager(int branchId, int managerId) throws SQLException {
        Branch branch = branchService.find(branchId);
        if (branch == null) {
            throw new BranchNotFoundException("branch id is wrong");
        }

        Employee employee = employeeService.find(managerId);
        if (employee == null) {
            throw new EmployeeNotFoundException("manager id is wrong");
        }

        if (employee.branch().id() != branchId) {
            throw new IllegalBranchManagerException("this employee doesn't work at this branch");
        }

        BranchManager branchManager = branchManagerRepository.findByBranchId(branchId);
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
