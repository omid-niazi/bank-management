package ir.bootcamp.bank.exceptions;

public class CardDisabledException extends Exception{
    public CardDisabledException() {
    }

    public CardDisabledException(String message) {
        super(message);
    }
}
