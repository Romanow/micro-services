package ru.romanow.services.order.exceptions;

public class CreateOrderException
        extends RuntimeException {
    public CreateOrderException(String message) {
        super(message);
    }
}
