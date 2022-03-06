package ir.bootcamp.bank.exceptions;

public class InvalidCardNumberException extends RuntimeException {
    public InvalidCardNumberException() {
    }

    public InvalidCardNumberException(String message) {
        super(message);
    }
}
