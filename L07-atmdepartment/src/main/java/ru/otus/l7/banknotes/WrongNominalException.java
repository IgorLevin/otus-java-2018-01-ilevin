package ru.otus.l7.banknotes;

public class WrongNominalException extends RuntimeException {
    public WrongNominalException() {
    }
    public WrongNominalException(String msg) {
        super(msg);
    }
}
