package com.skillbox.data.model;

import com.skillbox.exception.IncorrectTransactionType;
import com.skillbox.exception.WrongArgumentsRecurrentTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TransactionFactory {
    public static Transaction createTransaction(String idScoreTransaction,
                                                String idTransaction, String dateTransaction,
                                                String categoryTransaction,
                                                String amountTransaction,
                                                String typeTransaction, String values) {

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        switch (TypeTransaction.valueOf(typeTransaction)) {
            case Regular -> {
                return new TransactionRegular(
                        Integer.parseInt(idScoreTransaction),
                        Integer.parseInt(idTransaction),
                        LocalDateTime.parse(dateTransaction, formatter),
                        categoryTransaction,
                        BigDecimal.valueOf(Double.parseDouble(amountTransaction)),
                        TypeTransaction.valueOf(typeTransaction));
            }
            case Taxable -> {
                return new TransactionTaxable(Integer.parseInt(idScoreTransaction),
                        Integer.parseInt(idTransaction),
                        LocalDateTime.parse(dateTransaction, formatter),
                        categoryTransaction,
                        BigDecimal.valueOf(Double.parseDouble(amountTransaction)),
                        TypeTransaction.valueOf(typeTransaction),
                        Double.parseDouble(values)
                );
            }
            case Recurrent -> {
                List<String> meaning = new ArrayList<>();
                meaning.add(values);
                String patterns = "";
                String numberOfRepetitions = "";
                for (String result : meaning) {
                    String[] fragments = result.split(";");
                    if (fragments.length != 2) {
                        throw new WrongArgumentsRecurrentTransaction(
                                "Не верно указаны аргументы в транзакции.");
                    }
                    patterns = fragments[0];
                    numberOfRepetitions = fragments[1];
                }
                return new TransactionRecurrent(Integer.parseInt(idScoreTransaction),
                            Integer.parseInt(idTransaction),LocalDateTime.parse(dateTransaction, formatter),
                        categoryTransaction,
                            BigDecimal.valueOf(Double.parseDouble(amountTransaction)),
                            TypeTransaction.valueOf(typeTransaction),
                        RecurrencePattern.of(patterns), Integer.parseInt(numberOfRepetitions));
            }
            case ForeignCurrency -> {
                return new TransactionForeignCurrency(
                        Integer.parseInt(idScoreTransaction),
                        Integer.parseInt(idTransaction),
                        LocalDateTime.parse(dateTransaction, formatter),
                        categoryTransaction,
                        BigDecimal.valueOf(Double.parseDouble(amountTransaction)),
                        TypeTransaction.valueOf(typeTransaction),
                        BigDecimal.valueOf(Double.parseDouble(values))
                );
            }
            case Commentable -> {
                List<String> meaning = new ArrayList<>();
                List<String> list = new ArrayList<>();
                meaning.add(values);
                for (String result : meaning) {
                    String[] fragments = result.split(";");
                    list.addAll(Arrays.asList(fragments));
                }
                return new TransactionCommentable(Integer.parseInt(idScoreTransaction),
                        Integer.parseInt(idTransaction),
                        LocalDateTime.parse(dateTransaction, formatter),
                        categoryTransaction,
                        BigDecimal.valueOf(Double.parseDouble(amountTransaction)),
                        TypeTransaction.valueOf(typeTransaction),
                        list);
            }
            default -> throw new IncorrectTransactionType("Неизвестный тип транзакции");
        }
    }
}

