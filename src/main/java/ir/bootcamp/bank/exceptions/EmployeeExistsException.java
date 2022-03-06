package ir.bootcamp.bank.exceptions;

public class EmployeeExistsException extends RuntimeException {
    public EmployeeExistsException() {
    }

    public EmployeeExistsException(String message) {
        super(message);
    }
}
