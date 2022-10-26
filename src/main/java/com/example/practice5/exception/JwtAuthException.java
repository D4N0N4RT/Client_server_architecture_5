package com.example.practice5.exception;

import javax.naming.AuthenticationException;

public class JwtAuthException extends AuthenticationException {
    public JwtAuthException(String explanation) {
        super(explanation);
    }
}
