package com.tnh.baseware.core.exceptions;

public class BWCNotFoundException extends BasewareCoreException {

    public BWCNotFoundException(String message) {
        super(message, 404);
    }

    public BWCNotFoundException(String message, Throwable cause) {
        super(message, cause, 404);
    }
}
