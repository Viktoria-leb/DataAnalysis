package com.skillbox.controller.dto;

import com.skillbox.controller.option.GroupOption;
import com.skillbox.data.model.Account;
import com.skillbox.data.model.Transaction;

import java.math.BigDecimal;
import java.time.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupingTransactionDto {
    private GroupOption groupOption;

    public void setGroupOption(GroupOption groupOption) {
        this.groupOption = groupOption;
    }

    public GroupOption getGroupOption() {
        return groupOption;
    }

    public Map<String, List<Transaction>> grouping(GroupOption option,
                                                   List<Transaction> transactions,
                                                   List<Account> accounts) {
        return switch (option) {
            case EXIT -> new HashMap<>();
            case GROUP_BY_MONTH -> groupTransactionsByMonth(transactions);
            case GROUP_BY_YEAR -> groupTransactionsByYEAR(transactions);
            case GROUP_BY_DAY_OF_WEEK -> groupTransactionsByDayOfWeek(transactions);
            case GROUP_CATEGORIES -> groupTransactionsCategories(transactions);
            case COUNT_INCOME_AND_EXPENSES -> groupTransactionsCountIncomeAndExpenses(transactions);
            case GROUP_BY_ACCOUNT_TYPE -> groupTransactionsByAccountType(transactions, accounts);
            case GROUP_BY_USER_ID -> groupTransactionsByUserID(transactions);
        };
    }

    private Map<String, List<Transaction>> groupTransactionsByMonth
            (List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(transaction -> {
                    Month month = Month.from(transaction.getDateTransaction());
                    return month.toString();
                }));
    }

    private Map<String, List<Transaction>> groupTransactionsByYEAR
            (List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(transaction -> {
                    Year year = Year.from(transaction.getDateTransaction());
                    return year.toString();
                }));
    }

    private Map<String, List<Transaction>> groupTransactionsByDayOfWeek
            (List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> {
                            LocalDateTime date = transaction.getDateTransaction();
                            DayOfWeek dayOfWeek = date.getDayOfWeek();
                            String dayName = dayOfWeek.toString().toLowerCase();
                            return dayName.substring(0, 1).toUpperCase()
                                    + dayName.substring(1);
                        }
                ));
    }

    private Map<String, List<Transaction>> groupTransactionsCategories
            (List<Transaction> transactions) {
        return transactions.stream().collect(Collectors.groupingBy(
                Transaction::getCategoryTransaction
        ));
    }

    private Map<String, List<Transaction>> groupTransactionsCountIncomeAndExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(transaction -> {
                            BigDecimal amount = transaction.amountTransaction();
                            return amount.compareTo(BigDecimal.ZERO) > 0 ? "income" : "expense";
                        }
                ));
    }

    private Map<String, List<Transaction>> groupTransactionsByAccountType(
            List<Transaction> transactions, List<Account> accounts) {


        Map<Integer, String> accountTypeMap = accounts.stream()
                .collect(Collectors.toMap(
                        Account::getAccountId,
                        account -> switch (account.getAccountType()) {
                            case CREDIT -> "Кредитный счет";
                            case SAVINGS -> "Сберегательный счет";
                            case CHECKING -> "Текущий счет";
                        }

                ));

        return transactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> {
                            Integer accountId = transaction.getIdScoreTransaction();
                            return accountTypeMap.getOrDefault(accountId, " ");
                        }
                ));
    }

    private Map<String, List<Transaction>> groupTransactionsByUserID
            (List<Transaction> transactions) {
        return transactions.stream().collect(Collectors.groupingBy(
                transaction -> String.valueOf(transaction.getIdScoreTransaction())
        ));
    }

}