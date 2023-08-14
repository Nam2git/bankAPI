package com.ws.tla.exception;

public class BankServiceApiException extends Exception {

    String message;

    public BankServiceApiException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}