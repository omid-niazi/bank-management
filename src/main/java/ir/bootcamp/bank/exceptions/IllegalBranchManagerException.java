package ir.bootcamp.bank.exceptions;

public class IllegalBranchManagerException extends RuntimeException{
    public IllegalBranchManagerException() {
    }

    public IllegalBranchManagerException(String message) {
        super(message);
    }
}
