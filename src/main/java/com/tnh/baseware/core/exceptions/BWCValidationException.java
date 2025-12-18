package com.tnh.baseware.core.exceptions;

public class BWCValidationException extends BasewareCoreException {

    public BWCValidationException(String message) {
        super(message, 400);
    }

    public BWCValidationException(String message, Throwable cause) {
        super(message, cause, 400);
    }
}
