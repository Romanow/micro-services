package ru.romanow.services.order.exceptions;

public class EntityProcessException
        extends RuntimeException {
    public EntityProcessException(String message) {
        super(message);
    }
}
