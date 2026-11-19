package com.skillbox.service;

import com.skillbox.controller.dto.AggregateTransactionsDto;
import com.skillbox.controller.dto.GroupingTransactionDto;
import com.skillbox.controller.dto.TransactionFilterDto;
import com.skillbox.controller.option.AggregateOption;
import com.skillbox.controller.option.GroupOption;
import com.skillbox.controller.option.SearchOption;
import com.skillbox.data.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;


public record ServiceTransaction(List<Account> accounts, List<Transaction> transactions) implements TransactionService {

    @Override
    public Analytic calculateAnalytics(TransactionFilterDto transactionFilter,
                                       GroupingTransactionDto groupingFilter,
                                       AggregateTransactionsDto aggregateFilter) {
        List<Account> accounts = accounts();
        List<SearchOption> searchOption = transactionFilter.getOptionsList();
        List<Transaction> filteredTransactions = transactions();
        if (!searchOption.isEmpty()) {
            Predicate<Transaction> filterPredicate = transactionFilter.buildPredicate();
            filteredTransactions = transactions().stream()
                    .filter(filterPredicate)
                    .toList();
        }

        GroupOption groupOption = groupingFilter.getGroupOption();
        AggregateOption aggregateOption = aggregateFilter.getOption();

        Map<String, List<Transaction>> groupedTransactions = null;
        Map<String, Double> aggregatedResults = null;

        if (groupOption != null) {
            groupedTransactions = groupingFilter.grouping(groupOption, filteredTransactions, accounts);
        }

        if (aggregateOption != null) {
            if (groupedTransactions != null && !groupedTransactions.isEmpty()) {
                aggregatedResults = aggregateFilter.aggregate(aggregateOption, groupedTransactions);
            } else {
                aggregatedResults = aggregateFilter.aggregate(
                        aggregateOption,
                        Map.of("all", filteredTransactions)
                );
            }
        }

        return createAnalyticResult(
                LocalDateTime.now(),
                searchOption,
                groupOption,
                aggregateOption,
                transactionFilter,
                filteredTransactions,
                groupedTransactions,
                aggregatedResults
        );
    }

    private Analytic createAnalyticResult(LocalDateTime timestamp,
                                          List<SearchOption> searchOption,
                                          GroupOption groupOption,
                                          AggregateOption aggregateOption,
                                          TransactionFilterDto transactionFilter,
                                          List<Transaction> filteredTransactions,
                                          Map<String, List<Transaction>> groupedTransactions,
                                          Map<String, Double> aggregatedResults) {

        return buildAnalytic(timestamp, searchOption, groupOption, aggregateOption,
                transactionFilter, filteredTransactions, groupedTransactions, aggregatedResults);

    }

    private Analytic buildAnalytic(LocalDateTime timestamp,
                                   List<SearchOption> searchOption,
                                   GroupOption groupOption,
                                   AggregateOption aggregateOption,
                                   TransactionFilterDto transactionFilter,
                                   List<Transaction> filteredTransactions,
                                   Map<String, List<Transaction>> groupedTransactions,
                                   Map<String, Double> aggregatedResults) {
        if (aggregatedResults != null && groupedTransactions != null) {
            return new AnalyticGroupingAggregate(timestamp, searchOption, groupOption,
                    aggregateOption, transactionFilter, aggregatedResults);
        }
        if (groupedTransactions != null) {
            return new AnalyticGrouping(timestamp, searchOption, groupOption,
                    transactionFilter, groupedTransactions);
        }
        if (aggregatedResults != null) {
            return new AnalyticAggregate(timestamp, searchOption, aggregateOption,
                    transactionFilter, filteredTransactions);
        }
        if (!searchOption.isEmpty()) {
            return new AnalyticListSearch(timestamp, filteredTransactions, searchOption, transactionFilter);
        }
        return new AnalyticList(timestamp, filteredTransactions, searchOption, transactionFilter);
    }
}


