package ir.bootcamp.bank.service;

import ir.bootcamp.bank.model.Branch;
import ir.bootcamp.bank.repositories.BranchRepository;

import static ir.bootcamp.bank.util.ConsoleUtil.*;
import static ir.bootcamp.bank.util.ConsoleMessageType.*;

import java.sql.SQLException;

public class BranchService {

    private BranchRepository branchRepository;


    public BranchService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public void createBranch(String name, String address) throws SQLException {
        if (branchRepository.find(name) != null) {
            print("this name is already taken", error);
            return;
        }
        branchRepository.add(new Branch(name, address));
        print("branch created", info);
    }

    public Branch find(int branchId) throws SQLException {
        return branchRepository.find(branchId);
    }
}
