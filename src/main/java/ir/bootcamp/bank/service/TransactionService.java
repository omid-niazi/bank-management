package ir.bootcamp.bank.service;

import ir.bootcamp.bank.model.Transaction;
import ir.bootcamp.bank.repositories.TransactionRepository;

import java.sql.SQLException;

public class TransactionService {
    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void makeTransaction(Transaction transaction) throws SQLException {
        transactionRepository.add(transaction);
    }
}
