package com.denisyan.socks_must_flow.exception_handler;

/**
 * Duplicate login in repository
 */

public class LoginAlreadyExistException extends RuntimeException {
    public LoginAlreadyExistException(String message) {
        super(message);
    }
}
