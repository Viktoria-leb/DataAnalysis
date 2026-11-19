package com.skillbox.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.exception.DateBeforePayment;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class TransactionRecurrent extends Transaction implements Recurring {
    @JsonProperty("Переодичность повторений")
    private final RecurrencePattern patterns;
    @JsonProperty("Колличество повторений")
    private final Integer numberOfRepetitions;
    @JsonIgnore
    private final List<TransactionRecurrent> listSeries;

    public TransactionRecurrent(Integer idScoreTransaction,
                                Integer idTransaction,
                                LocalDateTime dateTransaction,
                                String categoryTransaction,
                                BigDecimal amountTransaction,
                                TypeTransaction typeTransaction,
                                RecurrencePattern pattern,
                                Integer numberOfRepetitions) {
        super(idScoreTransaction,
                idTransaction,
                dateTransaction,
                categoryTransaction,
                amountTransaction,
                typeTransaction);
        this.patterns = pattern;
        this.numberOfRepetitions = numberOfRepetitions;
        this.listSeries = new ArrayList<>();
    }

    public RecurrencePattern getPatterns() {
        return patterns;
    }

    public Integer getNumberOfRepetitions() {
        return numberOfRepetitions;
    }

    public List<TransactionRecurrent> getListSeries() {
        addListTransactionSeries();
        return listSeries;
    }

    @Override
    public LocalDateTime getPreviousOccurrence(LocalDateTime dateTime) {
        return dateIsAfter(dateTime);
    }


    @Override
    public LocalDateTime getNextOccurrence(LocalDateTime dateTime) {
        return dateIsAfter(dateTime).plus(getPatterns().getDuration());
    }


    private LocalDateTime dateIsAfter(LocalDateTime dateTime) {
        dateTimeBeforeDate(dateTime, getDateTransaction());
        LocalDateTime result = getDateTransaction();
        while (result.isBefore(dateTime)) {
            result = result.plus(getPatterns().getDuration());
        }
        return result;
    }

    @Override
    public BigDecimal getTransactionAmount(LocalDateTime dateTime) {
        dateTimeBeforeDate(dateTime, getDateTransaction());
        BigDecimal sum = BigDecimal.ZERO;
        for (TransactionRecurrent recurrent: getListSeries()){
            if(!recurrent.getDateTransaction().isAfter(dateTime)){
                sum = sum.add(getAmountTransaction());
            }
        }
        return sum;
    }

    private void dateTimeBeforeDate(LocalDateTime dateTime, LocalDateTime datePayment) {
        if (dateTime.isBefore(datePayment)) {
            throw new DateBeforePayment("Указана дата дo существования платежа");
        }
    }
    private void addListTransactionSeries(){
        LocalDateTime date = getDateTransaction();
        Duration day = getPatterns().getDuration();
        for(int i=0 ; i < getNumberOfRepetitions(); i++){
            LocalDateTime nextDate = date.plus(day.multipliedBy(i));
            int count = getNumberOfRepetitions() - i;
            if(!nextDate.isAfter(LocalDateTime.now())){
            listSeries.add(new TransactionRecurrent(getIdScoreTransaction(),
                    getIdTransaction(), nextDate, getCategoryTransaction(),
                    amountTransaction(),
                    getTypeTransaction(),
                    getPatterns(), count));
        }}
    }
    private boolean isWithinRange(LocalDateTime date, LocalDateTime startDate, LocalDateTime endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    @Override
    public boolean isExecutedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return getListSeries().stream()
                .anyMatch(transaction ->
                        isWithinRange(
                                transaction.getDateTransaction(),
                                startDate,
                                endDate
                        ));
    }

    @Override
    public String toString() {
        return "TransactionRecurrent " + toStrings() +
                "patterns " + patterns +
                ",numberOfRepetitions " + numberOfRepetitions;
    }

    @Override
    public BigDecimal amountTransaction() {
        return getAmountTransaction();
    }
}

