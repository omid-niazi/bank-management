package ir.bootcamp.bank.service;

import ir.bootcamp.bank.exceptions.AccountExistsException;
import ir.bootcamp.bank.exceptions.AccountNotEnoughBalanceException;
import ir.bootcamp.bank.exceptions.AccountNotFoundException;
import ir.bootcamp.bank.model.Account;
import ir.bootcamp.bank.model.Customer;
import ir.bootcamp.bank.repositories.AccountRepository;

import java.sql.SQLException;
import java.util.List;

public class AccountService {
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    void createAccount(String accountNumber, long amount, Customer customer) throws SQLException, AccountExistsException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            throw new AccountExistsException("this account number is already taken");
        }

        accountRepository.add(new Account(accountNumber, amount, customer));
    }

    void deposit(String accountNumber, long amount) throws SQLException, AccountNotFoundException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("there is not account with this account number");
        }

        Account updatedAccount = new Account(
                account.id(),
                account.accountNumber(),
                account.amount() + amount,
                account.customer()
        );
        accountRepository.update(updatedAccount);
    }

    void withdraw(String accountNumber, long amount) throws SQLException, AccountNotFoundException, AccountNotEnoughBalanceException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("there is not account with this account number");
        }

        if (account.amount() < amount) {
            throw new AccountNotEnoughBalanceException("your balance is not enough");
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

    Account find(String accountNumber) throws SQLException, AccountNotFoundException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("there is not account with this account number");
        }
        return account;
    }

    void removeAccount(String accountNumber) throws SQLException, AccountNotFoundException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("there is not account with this account number");
        }
        accountRepository.delete(account.id());
    }
}
