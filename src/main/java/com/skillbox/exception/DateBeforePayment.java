package com.skillbox.exception;

public class DateBeforePayment extends RuntimeException {
    public DateBeforePayment(String message) {
        super(message);
    }
}

