package ir.bootcamp.bank.model;

import java.sql.Date;

public class Card {
    private int id;
    private String cardNumber;
    private String cvv2;
    private Date expireDate;
    private String password;
    private Account account;
    private byte status;

    public Card(int id, String cardNumber, String cvv2, Date expireDate, String password, Account account, byte status) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.cvv2 = cvv2;
        this.expireDate = expireDate;
        this.password = password;
        this.account = account;
        this.status = status;
    }

    public Card(String cardNumber, String cvv2, Date expireDate, Account account, byte status) {
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

    public String cvv2() {
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

    public byte status() {
        return status;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
