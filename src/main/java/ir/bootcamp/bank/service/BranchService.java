package ir.bootcamp.bank.service;

import ir.bootcamp.bank.exceptions.BranchExistsException;
import ir.bootcamp.bank.model.Branch;
import ir.bootcamp.bank.repositories.BranchRepository;

import java.sql.SQLException;

public class BranchService {

    private BranchRepository branchRepository;


    public BranchService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public void createBranch(String name, String address) throws SQLException {
        if (branchRepository.find(name) != null) {
            throw new BranchExistsException("this name is already taken");
        }
        branchRepository.add(new Branch(name, address));
    }

    public Branch find(int branchId) throws SQLException {
        return branchRepository.find(branchId);
    }

    public Branch find(String branchName) throws SQLException {
        return branchRepository.find(branchName);
    }
}
