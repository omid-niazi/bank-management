package ir.bootcamp.bank.model;

public record BranchManager(int id, Branch branch, Employee manager) {

    public BranchManager(Branch branch, Employee manager) {
        this(Integer.MIN_VALUE, branch, manager);
    }
}
