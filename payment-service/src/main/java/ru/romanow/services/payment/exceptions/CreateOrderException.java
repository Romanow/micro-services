package ru.romanow.services.payment.exceptions;

public class CreateOrderException
        extends RuntimeException {
    public CreateOrderException(String message) {
        super(message);
    }
}
