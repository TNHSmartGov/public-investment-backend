package com.tnh.baseware.core.exceptions;

public class BWCJsonProcessingException extends BasewareCoreException {

    public BWCJsonProcessingException(String message) {
        super(message, 500);
    }

    public BWCJsonProcessingException(String message, Throwable cause) {
        super(message, cause, 500);
    }
}
