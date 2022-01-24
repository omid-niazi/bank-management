package ir.bootcamp.bank.model;

import java.sql.Date;

public class Card {
    private int id;
    private String cardNumber;
    private short cvv2;
    private Date expireDate;
    private String password = "";
    private Account account;
    private int failedAttempt = 0;
    private int status;

    public Card(int id, String cardNumber, short cvv2, Date expireDate, String password, Account account, int failedAttempt, int status) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.cvv2 = cvv2;
        this.expireDate = expireDate;
        this.password = password;
        this.account = account;
        this.failedAttempt = failedAttempt;
        this.status = status;
    }

    public Card(String cardNumber, short cvv2, Date expireDate, Account account, int status) {
        this.cardNumber = cardNumber;
        this.cvv2 = cvv2;
        this.expireDate = expireDate;
        this.account = account;
        this.status = status;
    }

    public Card(int id, String cardNumber) {
        this.id = id;
        this.cardNumber = cardNumber;
    }

    public int id() {
        return id;
    }

    public String cardNumber() {
        return cardNumber;
    }

    public short cvv2() {
        return cvv2;
    }

    public Date expireDate() {
        return expireDate;
    }

    public String password() {
        return password;
    }

    public Account account() {
        return account;
    }

    public int status() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int failedAttempt() {
        return failedAttempt;
    }

    public void setFailedAttempt(int f) {
        this.failedAttempt = f;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
