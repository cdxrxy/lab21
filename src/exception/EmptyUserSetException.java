package exception;

public class EmptyUserSetException extends RuntimeException {
    public EmptyUserSetException(String message) {
        super(message);
    }
}
