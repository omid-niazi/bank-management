package ir.bootcamp.bank.service;

import ir.bootcamp.bank.model.Branch;
import ir.bootcamp.bank.model.BranchManager;
import ir.bootcamp.bank.model.Employee;
import ir.bootcamp.bank.repositories.BranchManagerRepository;

import static ir.bootcamp.bank.util.ConsoleUtil.*;
import static ir.bootcamp.bank.util.ConsoleMessageType.*;

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
            print("branch id is wrong", error);
            return;
        }

        Employee employee = employeeService.find(managerId);
        if (employee == null) {
            print("manager id is wrong", error);
            return;
        }

        if (employee.branch().id() != branchId) {
            print("this employee doesn't work at this branch", error);
            return;
        }

        BranchManager branchManager = branchManagerRepository.findByBranchId(branchId);
        if (branchManager != null) {
            branchManagerRepository.update(new BranchManager(
                    branchManager.id(),
                    branchManager.branch(),
                    employee
            ));
            print("branch manager updated", success);
        } else {
            branchManagerRepository.add(new BranchManager(
                    branch,
                    employee
            ));
            print("branch manager added", success);
        }
    }
}
