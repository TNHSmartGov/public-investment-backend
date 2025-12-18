package com.tnh.baseware.core.exceptions;

public class BWCInternalServerException extends BasewareCoreException {

    public BWCInternalServerException(String message) {
        super(message, 500);
    }

    public BWCInternalServerException(String message, Throwable cause) {
        super(message, cause, 500);
    }
}
