package ir.bootcamp.bank.model;

public record Account(int id, String accountNumber, long amount, Customer customer) {

    public Account(String accountNumber, long amount, Customer customer) {
        this(Integer.MIN_VALUE, accountNumber, amount, customer);
    }
}
