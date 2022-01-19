package ir.bootcamp.bank.model;

public record Customer(int id, String name, String nationalCode, String phone) {
    public Customer(String name, String nationalCode, String phone) {
        this(Integer.MIN_VALUE, name, nationalCode, phone);
    }
}
