package com.tnh.baseware.core.exceptions;

public class BWCBusinessException extends BasewareCoreException {

    public BWCBusinessException(String message) {
        super(message, 400);
    }

    public BWCBusinessException(String message, Throwable cause) {
        super(message, cause, 400);
    }
}
