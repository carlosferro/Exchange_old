package com.ferro.exchange.web;

public class InvalidUsernameException extends RuntimeException {
    public InvalidUsernameException() {
    }

    public InvalidUsernameException(String userName) {
        super("UserName: " + userName + " is unavailable.");
    }
}
