package com.tnh.baseware.core.exceptions;

public class BWCAuthenticationException extends BasewareCoreException {

    public BWCAuthenticationException(String message) {
        super(message, 401);
    }

    public BWCAuthenticationException(String message, Throwable cause) {
        super(message, cause, 401);
    }
}
