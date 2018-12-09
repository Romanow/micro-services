package ru.romanow.services.store.exceptions;

public class OrderProcessException
        extends RuntimeException {
    public OrderProcessException(String message) {
        super(message);
    }
}
