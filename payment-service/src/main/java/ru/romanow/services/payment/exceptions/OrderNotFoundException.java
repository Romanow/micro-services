package ru.romanow.services.payment.exceptions;

public class OrderNotFoundException
        extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
