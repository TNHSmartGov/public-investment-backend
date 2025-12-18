package com.tnh.baseware.core.exceptions;

public class BWCParseDataException extends BasewareCoreException {

    public BWCParseDataException(String message) {
        super(message, 400);
    }

    public BWCParseDataException(String message, Throwable cause) {
        super(message, cause, 400);
    }
}
