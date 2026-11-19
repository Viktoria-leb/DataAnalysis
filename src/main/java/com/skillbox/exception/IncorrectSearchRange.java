package com.skillbox.exception;

public class IncorrectSearchRange extends RuntimeException{
    public IncorrectSearchRange() {
    }

    @Override
    public String getMessage() {
        return "Не верный диапозон для поиска.Введите данные заново.";
    }
}

