package com.skillbox.exception;

public class WrongArgumentsRecurrentTransaction extends RuntimeException{
    public WrongArgumentsRecurrentTransaction(String message) {
        super(message);
    }
}

