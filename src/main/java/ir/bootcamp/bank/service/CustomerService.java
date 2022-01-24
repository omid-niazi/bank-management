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

    public void cardValidation(String fromCardNumber, String toCardNumber, long amount) throws SQLException, InvalidCardNumberException, CardNotFoundException, AccountNotEnoughBalanceException {
        if (fromCardNumber.length() != 16 || toCardNumber.length() != 16) {
            throw new InvalidCardNumberException("card number must be 16 character");
        }

        Card fromCard = cardService.find(fromCardNumber);
        Card toCard = cardService.find(toCardNumber);
        if (fromCard.account().amount() < amount + 600) {
            throw new AccountNotEnoughBalanceException("your account balance is not enough");
        }
    }

    public void transfer(String fromCardNumber, String password, short cvv2, Date expireDate, long amount, String toCardNumber) throws SQLException, AccountNotEnoughBalanceException, AccountNotFoundException, CardAuthenticationException, CardNotFoundException, CardDisabledException {
        Card fromCard = cardService.find(fromCardNumber);
        if (fromCard.status() != 1) {
            throw new CardDisabledException("you enter wrong password more than 2 time, your card is disabled");
        }
        if (!fromCard.password().equals(password)) {
            cardService.attemptFailed(fromCard);
            throw new CardAuthenticationException("authentication failed your password is wrong");
        }
        if (fromCard.cvv2() != cvv2) {
            throw new CardAuthenticationException("authentication failed your cvv2 is wrong");
        }
        if (!fromCard.expireDate().toLocalDate().isEqual(expireDate.toLocalDate())) {
            throw new CardAuthenticationException("authentication failed expire date is wrong");
        }
        Card toCard = cardService.find(toCardNumber);
        cardService.enableCard(fromCard);
        accountService.withdraw(fromCard.account().accountNumber(), amount + 600);
        accountService.deposit(toCard.account().accountNumber(), amount);
        Transaction transaction = new Transaction(fromCard, toCard, amount, true);
        transactionService.makeTransaction(transaction);
    }

    public void changePassword(String cardNumber, String oldPassword, String newPassword) throws SQLException, CardNotFoundException, InvalidCardPasswordException {
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
