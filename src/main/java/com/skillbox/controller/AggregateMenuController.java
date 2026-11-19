package com.skillbox.controller;

import com.skillbox.controller.dto.AggregateTransactionsDto;
import com.skillbox.controller.option.AggregateOption;

public class AggregateMenuController extends AbstractMenuController<AggregateOption>{

    public AggregateMenuController() {
        super(AggregateOption.class, "Выберите способ агрегации транзакций");
    }

    public AggregateTransactionsDto getAggregateTransaction(AggregateTransactionsDto filter) {
        boolean isCompleted = false;
        while (!isCompleted) {
            AggregateOption option = selectMenu();
            filter = switch (option) {
                case CALCULATING_AMOUNT -> {
                    isCompleted = true;
                    yield calculatingAmount(filter);
                }
                case CALCULATING_AVERAGE_VALUE -> {
                    isCompleted = true;
                    yield calculatingAverageValue(filter);
                }
                case COUNTING_NUMBER -> {
                    isCompleted = true;
                    yield countingNumber(filter);
                }
            };
        }
        return filter;
    }

    private AggregateTransactionsDto countingNumber(AggregateTransactionsDto filter) {
        filter.setOption(AggregateOption.COUNTING_NUMBER);
        return filter;
    }

    private AggregateTransactionsDto calculatingAverageValue(AggregateTransactionsDto filter) {
    filter.setOption(AggregateOption.CALCULATING_AVERAGE_VALUE);
        return filter;
    }

    private AggregateTransactionsDto calculatingAmount(AggregateTransactionsDto filter) {
        filter.setOption(AggregateOption.CALCULATING_AMOUNT);
        return filter;
    }

}
