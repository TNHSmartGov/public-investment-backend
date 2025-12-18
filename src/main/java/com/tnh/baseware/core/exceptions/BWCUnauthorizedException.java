package com.tnh.baseware.core.exceptions;

public class BWCUnauthorizedException extends BasewareCoreException {

    public BWCUnauthorizedException(String message) {
        super(message, 401);
    }

    public BWCUnauthorizedException(String message, Throwable cause) {
        super(message, cause, 401);
    }
}
