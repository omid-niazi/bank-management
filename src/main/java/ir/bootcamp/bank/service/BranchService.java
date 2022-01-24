package ir.bootcamp.bank.service;

import ir.bootcamp.bank.exceptions.BranchExistsException;
import ir.bootcamp.bank.exceptions.BranchNotFoundException;
import ir.bootcamp.bank.model.Branch;
import ir.bootcamp.bank.repositories.BranchRepository;

import java.sql.SQLException;

public class BranchService {

    private BranchRepository branchRepository;


    public BranchService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public void createBranch(String name, String address) throws SQLException, BranchExistsException {
        if (branchRepository.find(name) != null) {
            throw new BranchExistsException("this name is already taken");
        }
        branchRepository.add(new Branch(name, address));
    }

    public Branch find(int branchId) throws SQLException, BranchNotFoundException {
        Branch branch = branchRepository.find(branchId);
        if (branch == null) {
            throw new BranchNotFoundException("branch id is wrong");
        }
        return branch;
    }

    public Branch find(String branchName) throws SQLException, BranchNotFoundException {
        Branch branch = branchRepository.find(branchName);
        if (branch == null) {
            throw new BranchNotFoundException("there is no branch with this name");
        }
        return branch;
    }
}
