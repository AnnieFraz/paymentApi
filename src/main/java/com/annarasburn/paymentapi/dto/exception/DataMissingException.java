package com.annarasburn.paymentapi.dto.exception;

public class DataMissingException extends RuntimeException {
    public DataMissingException(String message) {
        super(message);
    }
}
