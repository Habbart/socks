package com.denisyan.socks_must_flow.exception_handler;

/**
 * Socks don't exist in warehouse
 */

public class SocksNotFound extends RuntimeException {

    public SocksNotFound(String message) {
        super(message);
    }
}
