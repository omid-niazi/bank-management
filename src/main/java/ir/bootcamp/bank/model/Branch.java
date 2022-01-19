package ir.bootcamp.bank.model;

public record Branch(int id, String name, String address) {

    public Branch(String name, String address) {
        this(Integer.MIN_VALUE, name, address);
    }
}
