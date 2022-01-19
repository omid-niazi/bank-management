package ir.bootcamp.bank.service;

import java.sql.SQLException;

public class SuperUserService {
    private BranchService branchService;
    private BranchManagerService branchManagerService;

    public SuperUserService(BranchService branchService, BranchManagerService branchManagerService) {
        this.branchService = branchService;
        this.branchManagerService = branchManagerService;
    }

    public void createBranch(String name, String address) throws SQLException {
        branchService.createBranch(name, address);
    }

    public void changeBranchManager(int branchId, int managerId) throws SQLException {
        branchManagerService.changeBranchManager(branchId, managerId);
    }
}
