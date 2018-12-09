package ru.romanow.services.store.service;

import javax.annotation.Nonnull;

public class WarrantyProcessException
        extends RuntimeException {
    public WarrantyProcessException(String message) {
        super(message);
    }
}
