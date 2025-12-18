package com.tnh.baseware.core.exceptions;

public class BWCCacheSystemException extends BasewareCoreException {

    public BWCCacheSystemException(String message) {
        super(message, 500);
    }

    public BWCCacheSystemException(String message, Throwable cause) {
        super(message, cause, 500);
    }
}
