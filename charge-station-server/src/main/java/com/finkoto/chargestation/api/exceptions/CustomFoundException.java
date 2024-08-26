package com.finkoto.chargestation.api.exceptions;

public class CustomFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public CustomFoundException(String message) {
        super(message);
    }

}
