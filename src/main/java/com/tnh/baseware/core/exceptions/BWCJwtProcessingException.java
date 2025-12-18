package com.tnh.baseware.core.exceptions;

public class BWCJwtProcessingException extends BasewareCoreException {

    public BWCJwtProcessingException(String message) {
        super(message, 400);
    }

    public BWCJwtProcessingException(String message, Throwable cause) {
        super(message, cause, 400);
    }
}
