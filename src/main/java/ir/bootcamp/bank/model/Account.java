package ir.bootcamp.bank.model;

import javax.persistence.*;
import java.util.Objects;

import static ir.bootcamp.bank.dbutil.DatabaseConstants.*;


@Entity
@Table(name = ACCOUNT_TABLE_NAME)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ACCOUNT_COLUMN_ID)
    private Integer id;
    @Column(name = ACCOUNT_COLUMN_NUMBER, unique = true)
    private String accountNumber;
    @Column(name = ACCOUNT_COLUMN_AMOUNT)
    private Long amount;
    @OneToOne
    @JoinColumn(name = ACCOUNT_COLUMN_CUSTOMER_ID)
    private Customer customer;

    public Account() {
    }

    public Account(Integer id, String accountNumber, long amount, Customer customer) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.customer = customer;
    }

    public Account(String accountNumber, long amount, Customer customer) {
        this.id = Integer.MIN_VALUE;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.customer = customer;
    }

    public Integer id() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String accountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public long amount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Customer customer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && Objects.equals(accountNumber, account.accountNumber) && Objects.equals(amount, account.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber, amount);
    }
}