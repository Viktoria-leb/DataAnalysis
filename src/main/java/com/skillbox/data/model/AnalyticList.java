package com.skillbox.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.controller.dto.TransactionFilterDto;
import com.skillbox.controller.option.SearchOption;

import java.time.LocalDateTime;
import java.util.List;

public class AnalyticList extends Analytic {
    @JsonProperty("Вывод всех транзакций")
    private final List<Transaction> transactions;

    public AnalyticList(LocalDateTime dateTime,
                        List<Transaction> transactions,  List<SearchOption> searchOption,
                        TransactionFilterDto transactionFilter) {
        super(dateTime, searchOption, transactionFilter);
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString()).append("\n");
        builder.append("Выведены все транзакции без группировки и функции агрегации ").append("\n");
        transactions.forEach(n -> builder.append(n.toString()).append("\n"));
        return builder.toString();
    }

}
