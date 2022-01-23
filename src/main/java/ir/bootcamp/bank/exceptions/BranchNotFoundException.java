package ir.bootcamp.bank.exceptions;

public class BranchNotFoundException extends RuntimeException{
    public BranchNotFoundException() {
    }

    public BranchNotFoundException(String message) {
        super(message);
    }
}
