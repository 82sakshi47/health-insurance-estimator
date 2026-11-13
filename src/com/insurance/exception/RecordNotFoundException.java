package com.insurance.exception;

/** Thrown when a customer or policy ID does not exist. */
public class RecordNotFoundException extends Exception {
    public RecordNotFoundException(String message) {
        super(message);
    }
}
