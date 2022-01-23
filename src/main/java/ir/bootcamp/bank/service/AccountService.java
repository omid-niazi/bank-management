package ir.bootcamp.bank.service;

import ir.bootcamp.bank.model.Account;
import ir.bootcamp.bank.model.Customer;
import ir.bootcamp.bank.repositories.AccountRepository;

import static ir.bootcamp.bank.util.ConsoleMessageType.*;
import static ir.bootcamp.bank.util.ConsoleUtil.*;

import java.sql.SQLException;
import java.util.List;

public class AccountService {
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    void createAccount(String accountNumber, long amount, Customer customer) throws SQLException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            print("account number is already taken", error);
            return;
        }

        accountRepository.add(new Account(accountNumber, amount, customer));
    }

    public void deposit(String accountNumber, long amount) throws SQLException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            print("account number is wrong", error);
            return;
        }

        Account updatedAccount = new Account(
                account.id(),
                account.accountNumber(),
                account.amount() + amount,
                account.customer()
        );
        accountRepository.update(updatedAccount);
    }

    void withdraw(String accountNumber, long amount) throws SQLException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            print("account number is wrong", error);
            return;
        }

        if (account.amount() < amount) {
            print("your balance is not enough", error);
            return;
        }

        Account updatedAccount = new Account(
                account.id(),
                account.accountNumber(),
                account.amount() - amount,
                account.customer()
        );
        accountRepository.update(updatedAccount);
    }

    List<Account> findCustomerAccounts(Customer customer) throws SQLException {
        return accountRepository.findByCustomerId(customer.id());
    }
}
