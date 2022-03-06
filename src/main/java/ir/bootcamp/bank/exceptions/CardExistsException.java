package ir.bootcamp.bank.exceptions;

public class CardExistsException extends RuntimeException{
    public CardExistsException() {
    }

    public CardExistsException(String message) {
        super(message);
    }
}
