package ir.bootcamp.bank.exceptions;

public class CustomerExistsException extends RuntimeException{
    public CustomerExistsException() {
    }

    public CustomerExistsException(String message) {
        super(message);
    }
}
