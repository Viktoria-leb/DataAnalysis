package com.skillbox.controller.option;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AggregateOption implements MenuOption {
    CALCULATING_AMOUNT("подсчёт суммы"),
    CALCULATING_AVERAGE_VALUE("подсчёт среднего значения"),
    COUNTING_NUMBER("подсчёт количества");
    private final String name;

    AggregateOption(String name) {
        this.name = name;
    }

    public static AggregateOption of(int option) {
        return OptionUtils.of(AggregateOption.class, option);
    }

    @Override
    public int getOption() {
        return ordinal();
    }

    @JsonValue
    @Override
    public String getName() {
        return this.name;
    }

}
