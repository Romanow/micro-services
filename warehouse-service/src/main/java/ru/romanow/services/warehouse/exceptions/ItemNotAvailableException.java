package ru.romanow.services.warehouse.exceptions;

public class ItemNotAvailableException
        extends RuntimeException {
    public ItemNotAvailableException(String message) {
        super(message);
    }
}
