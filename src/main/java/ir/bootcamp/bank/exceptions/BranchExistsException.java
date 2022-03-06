package ir.bootcamp.bank.exceptions;

public class BranchExistsException extends RuntimeException {
    public BranchExistsException() {
    }

    public BranchExistsException(String message) {
        super(message);
    }
}
