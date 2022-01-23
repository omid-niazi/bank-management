package ir.bootcamp.bank.model;

public record Transaction(int id, Card fromCard, Card toCard, long amount, boolean isOutGoing) {

    public Transaction(Card fromCard, Card toCard, Long amount, boolean isOutGoing) {
        this(Integer.MIN_VALUE, fromCard, toCard, amount, isOutGoing);
    }
}
