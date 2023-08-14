package com.ws.tla.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(value = {NoDataException.class})
    public ResponseEntity<Object> resourceNotFoundException(NoDataException ex) {
        return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.OK);
    }

    @ExceptionHandler(value = {BankServiceApiException.class})
    public ResponseEntity<Object> bankSvcApiException(BankServiceApiException ex) {
        return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.OK);
    }
}
