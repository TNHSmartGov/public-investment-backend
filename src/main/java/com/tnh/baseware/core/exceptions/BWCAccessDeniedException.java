package com.tnh.baseware.core.exceptions;

public class BWCAccessDeniedException extends BasewareCoreException {

    public BWCAccessDeniedException(String message) {
        super(message, 403);
    }

    public BWCAccessDeniedException(String message, Throwable cause) {
        super(message, cause, 403);
    }
}
