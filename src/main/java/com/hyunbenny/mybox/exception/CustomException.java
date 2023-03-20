package com.hyunbenny.mybox.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class CustomException extends RuntimeException {

    public abstract int getStatusCode();

    private final Map<String, String> validation = new HashMap<>();

    public CustomException() {
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }

}
