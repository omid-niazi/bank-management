package ir.bootcamp.bank.exceptions;

public class EmployeeInvalidPasswordException extends RuntimeException {
    public EmployeeInvalidPasswordException() {
    }

    public EmployeeInvalidPasswordException(String message) {
        super(message);
    }
}
