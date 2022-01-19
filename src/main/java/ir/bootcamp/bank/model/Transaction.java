package ir.bootcamp.bank.model;

public record Transaction(int id, Card fromCard, Card toCard, long amount, boolean isOutGoing,
                          TransactionType transactionType) {

    public Transaction(Card fromCard, Card toCard, Long amount, boolean isOutGoing) {
        this(Integer.MIN_VALUE, fromCard, toCard, amount, isOutGoing, TransactionType.transfer);
    }

    public Transaction(Card fromCard, Long amount, boolean isOutGoing) {
        this(Integer.MIN_VALUE, fromCard, null, amount, isOutGoing, TransactionType.cash);
    }

    public Transaction(int id, Card fromCard, Card toCard, Long amount, boolean isOutGoing) {
        this(id, fromCard, toCard, amount, isOutGoing, TransactionType.transfer);
    }

    public Transaction(int id, Card fromCard, Long amount, boolean isOutGoing) {
        this(id, fromCard, null, amount, isOutGoing, TransactionType.cash);
    }
}
