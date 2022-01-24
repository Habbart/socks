package com.denisyan.socks_must_flow.exception_handler;

public class SocksNotFound extends RuntimeException{

    public SocksNotFound(String message) {
        super(message);
    }
}
