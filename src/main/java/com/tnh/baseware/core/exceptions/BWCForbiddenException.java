package com.tnh.baseware.core.exceptions;

public class BWCForbiddenException extends BasewareCoreException {

    public BWCForbiddenException(String message) {
        super(message, 403);
    }

    public BWCForbiddenException(String message, Throwable cause) {
        super(message, cause, 403);
    }
}
