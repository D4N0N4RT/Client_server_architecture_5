package com.example.practice5.exception;

public class WrongAuthorityException extends Exception {
    public WrongAuthorityException(String message) {
        super(message);
    }
}