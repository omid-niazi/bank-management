package ir.bootcamp.bank.service;

import ir.bootcamp.bank.model.Account;
import ir.bootcamp.bank.model.Card;
import ir.bootcamp.bank.model.Customer;
import ir.bootcamp.bank.model.Transaction;
import ir.bootcamp.bank.repositories.CustomerRepository;

import static ir.bootcamp.bank.util.ConsoleUtil.*;
import static ir.bootcamp.bank.util.ConsoleMessageType.*;

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
            print("card numbers are not valid", error);
            return false;
        }

        if (cardService.find(fromCard) == null || cardService.find(toCard) == null) {
            print("entered card doesn't exists", error);
            return false;
        }

        return true;
    }

    public void transfer(String fromCardNumber, String password, String cvv2, Date expireDate, long amount, String toCardNumber) throws SQLException {
        Card fromCard = cardService.find(fromCardNumber);
        if (!fromCard.password().equals(password) || !fromCard.cvv2().equals(cvv2) || !fromCard.expireDate().equals(expireDate)) {
            print("authentication failed", error);
            return;
        }
        if (fromCard.account().amount() < amount) {
            print("your money is not enough", error);
            return;
        }
        Card toCard = cardService.find(toCardNumber);
        accountService.withdraw(fromCard.account().accountNumber(), amount);
        accountService.deposit(toCard.account().accountNumber(), amount);
        Transaction transaction = new Transaction(fromCard, toCard, amount, true);
        transactionService.makeTransaction(transaction);
        print("transaction success", success);
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
