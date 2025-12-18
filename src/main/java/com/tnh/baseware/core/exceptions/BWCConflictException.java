package com.tnh.baseware.core.exceptions;

public class BWCConflictException extends BasewareCoreException {

    public BWCConflictException(String message) {
        super(message, 409);
    }

    public BWCConflictException(String message, Throwable cause) {
        super(message, cause, 409);
    }
}
