package com.skillbox.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.controller.dto.TransactionFilterDto;
import com.skillbox.controller.option.GroupOption;
import com.skillbox.controller.option.SearchOption;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class AnalyticGrouping extends Analytic {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("Функция группировки")
    private final GroupOption groupOption;
    @JsonProperty("Вывод аналитики")
    private final Map<String, List<Transaction>> map;

    public AnalyticGrouping(LocalDateTime dateTime,
                            List<SearchOption> searchOption,
                            GroupOption groupOption,
                            TransactionFilterDto transactionFilter,
                            Map<String, List<Transaction>> map) {
        super(dateTime, searchOption, transactionFilter);
        this.groupOption = groupOption;
        this.map = map;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString()).append("\n");
        builder.append(String.format("%-35s %-35s ",
                "Группировка по: ", groupOption.getName())).append("\n");
        builder.append("-----------------------------------------------------").append("\n");
        builder.append("Аналитика").append("\n");
        map.forEach((key, value) -> {
            builder.append(key).append(": ").append("\n");
            value.forEach(item -> builder.append(String.format("%10s"
                    , item.toString())).append(" ").append("\n"));
        });
        return builder.toString();
    }


}
