package com.finkoto.chargestation.api.exceptions;

public class CustomPaymentRequiredException extends  CustomFoundException{
    public CustomPaymentRequiredException(String message) {
        super(message);
    }
}
