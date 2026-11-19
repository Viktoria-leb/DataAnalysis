package com.skillbox.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.controller.dto.TransactionFilterDto;
import com.skillbox.controller.option.AggregateOption;
import com.skillbox.controller.option.GroupOption;
import com.skillbox.controller.option.SearchOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyticGroupingAggregate extends Analytic {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("Функция группировки")
    private final GroupOption groupOption;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("Функция агрегации")
    private final AggregateOption aggregateOption;
    private final Map<String, Double> aggregate;
    @JsonProperty("Вывод аналитики")
    private final Map<String,String> aggregateJson;

    public AnalyticGroupingAggregate(LocalDateTime dateTime,
                                     List<SearchOption> searchOption,
                                     GroupOption groupOption,
                                     AggregateOption aggregateOption,
                                     TransactionFilterDto transactionFilter,
                                     Map<String, Double> aggregate) {
        super(dateTime, searchOption, transactionFilter);
        this.groupOption = groupOption;
        this.aggregateOption = aggregateOption;
        this.aggregate = aggregate;
        aggregateJson = new HashMap<>();
    }

    public Map<String, String> getAggregateJson() {
        addMapJson();
        return aggregateJson;
    }

    private void addMapJson(){
        aggregate.forEach((key, value) -> {
            String valueString = String.format("%.2f",value);;
            if (AggregateOption.CALCULATING_AMOUNT.equals(aggregateOption)) {
                aggregateJson.put(key,
                        valueString.replace(",",".") +" руб");
            }
            if (AggregateOption.CALCULATING_AVERAGE_VALUE.equals(aggregateOption)) {

               aggregateJson.put(key, valueString.replace(",",".") + " руб");
            }
            if(AggregateOption.COUNTING_NUMBER.equals(aggregateOption)){
              aggregateJson.put(key, String.valueOf(value.intValue()));
            }
    });}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString()).append("\n");
        builder.append(String.format("%-35s %-35s ",
                "Группировка по: ", groupOption.getName())).append("\n");
        builder.append(String.format("%-35s %-35s ",
                "Функция по группировке: ", aggregateOption.getName())).append("\n");
        builder.append("-----------------------------------------------------").append("\n");
        builder.append("Аналитика").append("\n");
        aggregate.forEach((key, value) -> {
            if (AggregateOption.CALCULATING_AMOUNT.equals(aggregateOption)) {
                builder.append(
                                String.format("%-35s %-35.2f %-15s", key, value, "руб"))
                        .append("\n");
            }
            if (AggregateOption.CALCULATING_AVERAGE_VALUE.equals(aggregateOption)) {
                builder.append(String.format("%-35s %-35f %-15s", key, value, "руб"))
                        .append("\n");
            }
            if(AggregateOption.COUNTING_NUMBER.equals(aggregateOption)){
                builder.append(String.format("%-35s %-35d", key, value.intValue()))
                        .append("\n");
            }
        });
        return builder.toString();
    }

}
