package ir.bootcamp.bank.exceptions;

public class InvalidCardPasswordException extends RuntimeException {
    public InvalidCardPasswordException() {
    }

    public InvalidCardPasswordException(String message) {
        super(message);
    }
}
