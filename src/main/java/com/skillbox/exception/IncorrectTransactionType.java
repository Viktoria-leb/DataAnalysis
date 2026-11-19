package com.skillbox.exception;

public class IncorrectTransactionType extends RuntimeException{
    public IncorrectTransactionType(String message) {
        super(message);
    }
}

