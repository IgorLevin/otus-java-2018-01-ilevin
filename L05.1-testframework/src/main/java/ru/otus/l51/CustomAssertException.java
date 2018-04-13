package ru.otus.l51;

public class CustomAssertException extends RuntimeException {

    public CustomAssertException(String msg) {
        super(msg);
    }

    public CustomAssertException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
