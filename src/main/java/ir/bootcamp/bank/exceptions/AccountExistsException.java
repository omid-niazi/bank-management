package ir.bootcamp.bank.exceptions;

public class AccountExistsException extends RuntimeException{
    public AccountExistsException() {
    }

    public AccountExistsException(String message) {
        super(message);
    }
}
