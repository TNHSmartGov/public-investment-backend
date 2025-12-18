package com.tnh.baseware.core.exceptions;

public class BWCRefreshTokenFailedException extends BasewareCoreException {

    public BWCRefreshTokenFailedException(String message) {
        super(message, 401);
    }

    public BWCRefreshTokenFailedException(String message, Throwable cause) {
        super(message, cause, 401);
    }
}
