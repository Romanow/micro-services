package ru.romanow.services.order.exceptions;

public class WarehouseProcessingException
        extends RuntimeException {
    public WarehouseProcessingException(String message) {
        super(message);
    }
}
