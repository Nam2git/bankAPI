package com.ws.tla.exception;

public class NoDataException extends Exception {

    String message;

    public NoDataException(String message) {
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
