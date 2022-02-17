package com.denisyan.socks_must_flow.security;

/**
 * Authorization token entity
 */

public class AuthToken {

    private String token;

    public AuthToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
