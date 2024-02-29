package com.example.photostudio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CannotPerformOperationException extends RuntimeException {
    public CannotPerformOperationException(String operationName, String resourceName, String fieldName, String fieldValue) {
        super(String.format("Cannot perform %s on %s with %s: '%s'",operationName,resourceName,fieldName,fieldValue));
    }
}
