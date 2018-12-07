package ru.romanow.services.store.service;

public class UserNotFoundException
        extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
