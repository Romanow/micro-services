package ru.romanow.services.store.exceptions;

public class UserNotFoundException
        extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
