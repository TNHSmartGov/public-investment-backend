package com.tnh.baseware.core.exceptions;

public class BWCInvalidTokenException extends BasewareCoreException {

    public BWCInvalidTokenException(String message) {
        super(message, 401);
    }

    public BWCInvalidTokenException(String message, Throwable cause) {
        super(message, cause, 401);
    }
}
