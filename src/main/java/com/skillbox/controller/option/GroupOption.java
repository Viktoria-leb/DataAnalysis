package com.skillbox.controller.option;

import com.fasterxml.jackson.annotation.JsonValue;

public enum GroupOption implements MenuOption {
    EXIT("вернуться назад(без группировки)"),
    GROUP_BY_MONTH("группировать по месяцам"),
    GROUP_BY_YEAR("группировать по годам"),
    GROUP_BY_DAY_OF_WEEK("группировать по дню недели"),
    GROUP_CATEGORIES("группировать по категории"),
    COUNT_INCOME_AND_EXPENSES("считать доходы и расходы"),
    GROUP_BY_ACCOUNT_TYPE("группировать по типу счёта"),
    GROUP_BY_USER_ID("группировать по ID пользователя");


    private final String name;

    GroupOption(String name) {
        this.name = name;
    }

    public static GroupOption of(int option) {
        return OptionUtils.of(GroupOption.class, option);
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
