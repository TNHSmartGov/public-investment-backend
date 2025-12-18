package com.tnh.baseware.core.exceptions;

public class BWCSQLCustomException extends BasewareCoreException {

    public BWCSQLCustomException(String message) {
        super(message, 500);
    }

    public BWCSQLCustomException(String message, Throwable cause) {
        super(message, cause, 500);
    }
}
