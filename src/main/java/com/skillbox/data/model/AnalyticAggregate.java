package com.skillbox.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.controller.dto.TransactionFilterDto;
import com.skillbox.controller.option.AggregateOption;
import com.skillbox.controller.option.SearchOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AnalyticAggregate extends Analytic {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("Функция агрегации")
    private final AggregateOption aggregateOption;
    private final List<Transaction> filteredTransactions;
    @JsonProperty("Вывод аналитики")
    private final Map<Integer, String> aggregate;

    public AnalyticAggregate(LocalDateTime dateTime,
                             List<SearchOption> searchOption,
                             AggregateOption aggregateOption,
                             TransactionFilterDto transactionFilter,
                             List<Transaction> filteredTransactions) {
        super(dateTime,searchOption,transactionFilter);
        this.aggregateOption = aggregateOption;
        this.filteredTransactions = filteredTransactions;
        aggregate = new HashMap<>();
    }


    public Map<Integer, String> getAggregate() {
        addMapAggregation(aggregateOption, filteredTransactions);
        return aggregate;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString()).append("\n");
        builder.append(String.format("%-35s %-35s ",
                "Функция по агрегации: ", aggregateOption.getName())).append("\n");
        builder.append("-----------------------------------------------------").append("\n");
        builder.append("Аналитика").append("\n");
        builder.append(aggregationFunction(aggregateOption));
        return builder.toString();
    }

    private void addMapAggregation(AggregateOption aggregateOption,
                                   List<Transaction> transactions){
        switch (aggregateOption) {
            case CALCULATING_AMOUNT, CALCULATING_AVERAGE_VALUE -> transactions.forEach(n ->
                aggregate.put(n.getIdTransaction(), n.amountTransaction()+ " руб"));
            case COUNTING_NUMBER -> transactions.forEach(n ->
                   aggregate.put(n.getIdTransaction(), String.valueOf(1)));
        }
    }

    private StringBuilder aggregationFunction(AggregateOption aggregateOption) {
        StringBuilder builder = new StringBuilder();
        switch (aggregateOption) {
            case CALCULATING_AMOUNT, CALCULATING_AVERAGE_VALUE ->
                    getAggregate().forEach((key, value) ->
                            builder.append(String.format("%-10d %-35s %-25s",
                            key, value
                            , " руб.")).append("\n"));
            case COUNTING_NUMBER ->
                    getAggregate().forEach((key, value) ->
                            builder.append(String.format("%-10d %-10d",
                            key, 1)).append("\n"));
        }
        return builder;
    }


}
