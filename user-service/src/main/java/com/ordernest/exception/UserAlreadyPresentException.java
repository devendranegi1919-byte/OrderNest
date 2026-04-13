package com.ordernest.exception;

public class UserAlreadyPresentException extends RuntimeException {
    public UserAlreadyPresentException(String s) {
        super(s);
    }
}
