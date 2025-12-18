package com.tnh.baseware.core.exceptions;

public class BWCRateLimitException extends BasewareCoreException {
    public BWCRateLimitException(String message) {
        super(message, 429);
    }

    public BWCRateLimitException(String message, Throwable cause) {
        super(message, cause, 429);
    }

}
