package mk.ukim.finki.ib.authentication.model.exceptions;

public class InvalidArgumentsException extends RuntimeException {
    public InvalidArgumentsException(final String message) {
        super(String.format("Invalid arguments exception: %s", message));
    }
}
