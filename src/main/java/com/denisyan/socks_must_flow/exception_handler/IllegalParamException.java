package com.denisyan.socks_must_flow.exception_handler;

/**
 * Incorrect params exception
 */
public class IllegalParamException extends RuntimeException {

    public IllegalParamException(String message) {
        super(message);
    }
}
