package ir.bootcamp.bank.model;

public record Employee(int id, String name, String password, Employee directManager, Branch branch) {

    public Employee(String name, String password, Employee directManager, Branch branch) {
        this(Integer.MIN_VALUE, name, password, directManager, branch);
    }
}
