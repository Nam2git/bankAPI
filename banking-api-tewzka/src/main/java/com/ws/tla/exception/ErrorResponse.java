package com.ws.tla.exception;

import java.util.Arrays;
import java.util.List;

public class ErrorResponse {

    private Long timestamp;
    private String errorCode;
    private String message;
    private List<String> errors;
    private String url;

    public ErrorResponse(String errorCode, String message, List<String> errors, String url) {
        super();
        this.errorCode = errorCode;
        this.errors = errors;
        this.message = message;
        this.url = url;
        this.timestamp = System.currentTimeMillis();
    }

    public ErrorResponse(String errorCode, String message, String error, String url) {
        super();
        this.errorCode = errorCode;
        this.errors = Arrays.asList(error);
        this.message = message;
        this.url = url;
        this.timestamp = System.currentTimeMillis();
    }

    public ErrorResponse(String errorCode, String message, String url) {
        super();
        this.errorCode = errorCode;
        this.message = message;
        this.url = url;
        this.timestamp = System.currentTimeMillis();
    }

}
