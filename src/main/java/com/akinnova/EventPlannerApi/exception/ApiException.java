package com.akinnova.EventPlannerApi.exception;

public class ApiException extends RuntimeException{
    public ApiException(String message) {
        super(message);
    }
}
