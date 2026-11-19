package com.skillbox.controller.dto;

import com.skillbox.controller.option.AggregateOption;
import com.skillbox.data.model.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AggregateTransactionsDto {
    private AggregateOption option;

    public void setOption(AggregateOption option) {
        this.option = option;
    }

    public AggregateOption getOption() {
        return option;
    }

    public Map<String, Double> aggregate(AggregateOption options, Map<String, List<Transaction>> map) {
        return switch (options) {
            case CALCULATING_AMOUNT -> calculatingAmount(map);
            case CALCULATING_AVERAGE_VALUE -> calculatingAverageValue(map);
            case COUNTING_NUMBER -> calculatingNumber(map);
        };
    }

    private Map<String, Double> calculatingAmount(Map<String, List<Transaction>> map) {
        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(Transaction::amountTransaction)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                                .setScale(2, RoundingMode.HALF_EVEN)
                                .doubleValue()
                ));
    }

    private Map<String, Double> calculatingAverageValue(Map<String, List<Transaction>> map) {
        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(Transaction::amountTransaction)
                                .mapToDouble(BigDecimal::doubleValue)
                                .average().orElse(0.0)));
    }

    private Map<String, Double> calculatingNumber(Map<String, List<Transaction>> map) {
        Map<String, Double> result = new HashMap<>();
        for (Map.Entry<String, List<Transaction>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<Transaction> transactions = entry.getValue();
            result.put(key, (double) transactions.size());
        }
        return result;
    }

}
