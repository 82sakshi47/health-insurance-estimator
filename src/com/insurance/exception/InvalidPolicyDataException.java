package com.insurance.exception;

/** Thrown when customer or policy data fails validation. */
public class InvalidPolicyDataException extends Exception {
    public InvalidPolicyDataException(String message) {
        super(message);
    }
}
