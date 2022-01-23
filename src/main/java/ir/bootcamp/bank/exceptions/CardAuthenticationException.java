package ir.bootcamp.bank.exceptions;

public class CardAuthenticationException extends RuntimeException {
    public CardAuthenticationException() {
    }

    public CardAuthenticationException(String message) {
        super(message);
    }
}
