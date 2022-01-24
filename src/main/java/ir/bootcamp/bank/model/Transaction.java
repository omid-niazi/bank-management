package ir.bootcamp.bank.model;

import java.sql.Timestamp;

public record Transaction(int id, Card fromCard, Card toCard, long amount, boolean isOutGoing, Timestamp timestamp) {

    public Transaction(Card fromCard, Card toCard, Long amount, boolean isOutGoing, Timestamp timestamp) {
        this(Integer.MIN_VALUE, fromCard, toCard, amount, isOutGoing, timestamp);
    }
}
