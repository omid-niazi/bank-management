package ir.bootcamp.bank.exceptions;

public class AccountNotEnoughBalanceException extends RuntimeException{
    public AccountNotEnoughBalanceException() {
    }

    public AccountNotEnoughBalanceException(String message) {
        super(message);
    }
}
