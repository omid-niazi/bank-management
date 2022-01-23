package ir.bootcamp.bank.service;

import ir.bootcamp.bank.exceptions.*;
import ir.bootcamp.bank.model.Card;
import ir.bootcamp.bank.model.Customer;
import ir.bootcamp.bank.model.Transaction;
import ir.bootcamp.bank.repositories.CustomerRepository;

import java.sql.Date;
import java.sql.SQLException;

public class CustomerService {
    private CustomerRepository customerRepository;
    private CardService cardService;
    private AccountService accountService;
    private TransactionService transactionService;

    public CustomerService(CustomerRepository customerRepository, CardService cardService, AccountService accountService, TransactionService transactionService) {
        this.customerRepository = customerRepository;
        this.cardService = cardService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public boolean cardsAreValid(String fromCard, String toCard) throws SQLException {
        if (fromCard.length() != 16 || toCard.length() != 16) {
            throw new InvalidCardNumberException("card number must be 16 character");
        }

        if (cardService.find(fromCard) == null || cardService.find(toCard) == null) {
            throw new CardNotFoundException("make sure enterd card numbers are correct");
        }

        return true;
    }

    public void transfer(String fromCardNumber, String password, short cvv2, Date expireDate, long amount, String toCardNumber) throws SQLException {
        Card fromCard = cardService.find(fromCardNumber);
        if (!fromCard.password().equals(password)) {
            throw new CardAuthenticationException("authentication failed your password is wrong");
        }
        if (fromCard.cvv2() != cvv2) {
            throw new CardAuthenticationException("authentication failed your cvv2 is wrong");
        }
        if (!fromCard.expireDate().toLocalDate().isEqual(expireDate.toLocalDate())) {
            throw new CardAuthenticationException("authentication failed expire date is wrong");
        }
        if (fromCard.account().amount() < amount) {
            throw new AccountNotEnoughBalanceException("your account balance is not enough");
        }
        Card toCard = cardService.find(toCardNumber);
        accountService.withdraw(fromCard.account().accountNumber(), amount);
        accountService.deposit(toCard.account().accountNumber(), amount);
        Transaction transaction = new Transaction(fromCard, toCard, amount, true);
        transactionService.makeTransaction(transaction);
    }

    public void changePassword(String cardNumber, String oldPassword, String newPassword) throws SQLException {
        cardService.changePassword(cardNumber, oldPassword, newPassword);
    }

    public Customer find(int customerId) throws SQLException {
        return customerRepository.find(customerId);
    }

    public Customer find(String nationalCode) throws SQLException, CustomerNotFoundException {
        Customer customer = customerRepository.find(nationalCode);
        if (customer == null) {
            throw new CustomerNotFoundException("there is not customer with this national code");
        }
        return customer;
    }

    void createCustomer(String name, String nationalCode, String phone) throws CustomerExistsException, SQLException {
        if (customerRepository.find(nationalCode) != null) {
            throw new CustomerExistsException("a customer with this national code already exists");
        }
        Customer customer = new Customer(name, nationalCode, phone);
        customerRepository.add(customer);
    }
}
