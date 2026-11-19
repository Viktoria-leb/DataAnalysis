package com.skillbox.controller.dto;

import com.skillbox.controller.option.SearchOption;
import com.skillbox.data.model.*;
import com.skillbox.exception.DuplicateOptionWhenSearching;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Класс для хранения фильтра по транзакциям.
 */
public class TransactionFilterDto {

    private List<SearchOption> options;
    private LocalDate startDate;
    private LocalDate endDate;
    private String comment;
    private BigDecimal startAmount;
    private BigDecimal endAmount;
    private String category;

    public TransactionFilterDto() {
        this.options = new ArrayList<>();
    }

    public void setOptions() {
        this.options = new ArrayList<>();
        System.out.println("Все опции сброшены.");
    }

    public List<SearchOption> getOptionsList() {

        return removingNullValue(options);
    }

    public void addOption(SearchOption option) {
        if (getOptionsList().contains(option)) {
            options.remove(option);
            throw new DuplicateOptionWhenSearching(
                    "Выбранная опция: " + option.getName().toUpperCase()
                            + " транзакций ПОВТОРНА.\n" +
                            "Предыдущие данные по этой опции удалены.\n" +
                            "Введите новые данные для " + option.getName()
            );
        }
        options.add(option);
    }

    private List<SearchOption> removingNullValue(List<SearchOption> list) {
        List<SearchOption> options1 = new ArrayList<>();
        for (SearchOption option : list) {
            switch (option) {
                case SEARCH_BY_DATES -> {
                    if ((getStartDate() == null) && (getEndDate() == null)) {
                        break;
                    }
                    options1.add(option);
                }
                case SEARCH_BY_AMOUNT -> {
                    if ((getStartAmount() == null)
                            && (getEndAmount() == null)) {
                        break;
                    }
                    options1.add(option);
                }
                case SEARCH_BY_CATEGORY -> {
                    if (getCategory() == null) {
                        break;
                    }
                    options1.add(option);
                }
                case SEARCH_BY_COMMENT -> {
                    if (getComment() == null) {
                        break;
                    }
                    options1.add(option);
                }
            }
        }
        return options1;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setStartAmount(BigDecimal startAmount) {
        this.startAmount = startAmount;
    }

    public void setEndAmount(BigDecimal endAmount) {
        this.endAmount = endAmount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getComment() {
        return comment;
    }

    public BigDecimal getStartAmount() {
        return startAmount;
    }

    public BigDecimal getEndAmount() {
        return endAmount;
    }

    public String getCategory() {
        return category;
    }

    /**
     * Создает предикат для фильтрации транзакций по диапазону дат.
     * Также вернет те Recurring транзакции, которые будут
     * или были выполнены в указанный диапазон дат
     *
     * @return Предикат для фильтрации транзакций по диапазону дат.
     */
    private Predicate<Transaction> datePredicate() {
        return transaction -> {
            LocalDateTime date = transaction.getDateTransaction();
            LocalDateTime start = startDate == null ? null : startDate.atStartOfDay();
            LocalDateTime end = endDate == null ? null : endDate.atStartOfDay();
            return (start == null || !date.isBefore(start)) &&
                    (end == null || !date.isAfter(end));
        };
    }


    /**
     * Создает предикат для фильтрации транзакций по комментарию или его части.
     * Фильтруются только транзакции,
     * имплементирующие интерфейс Commentable. Если токен пустой или null,
     * то возвращается предикат, который всегда
     * вернет true
     *
     * @return Предикат для фильтрации транзакций по комментарию.
     */
    private Predicate<Transaction> commentPredicate() {
        return transaction ->
        {
            if (comment == null || comment.isEmpty()) {
                return transaction instanceof TransactionCommentable;
            } else {
                return transaction instanceof TransactionCommentable &&
                        ((TransactionCommentable) transaction).getComments().contains(comment);
            }
        };
    }


    /**
     * Создает предикат для фильтрации транзакций по диапазону суммы.
     *
     * @return Предикат для фильтрации транзакций по диапазону суммы.
     */
    private Predicate<Transaction> amountPredicate() {
        return transaction -> {
            BigDecimal sum = transaction.amountTransaction();
            boolean lowerBoundOk = (startAmount == null) || (sum.compareTo(startAmount) >= 0);
            boolean upperBoundOk = (endAmount == null) || (sum.compareTo(endAmount) <= 0);
            return lowerBoundOk && upperBoundOk;
        };
    }

    /**
     * Создает предикат для фильтрации транзакций по категории.
     *
     * @return Предикат для фильтрации транзакций по категории.
     */
    private Predicate<Transaction> categoryPredicate() {
        return transaction -> {
            String categoryTransaction = transaction.getCategoryTransaction();
            return categoryTransaction.contains(category);
        };
    }

    /**
     * Собирает предикат для фильтрации транзакции.
     *
     * @return Предикат для фильтрации транзакции.
     */
    public Predicate<Transaction> buildPredicate() {

        Predicate<Transaction> combinedPredicate = transaction -> true;
        for (SearchOption option : getOptionsList()) {
            Predicate<Transaction> currentPredicate = getPredicateForOption(option);
            if (currentPredicate != null) {
                combinedPredicate = combinedPredicate.and(currentPredicate);
            }
        }
        return combinedPredicate;
    }


    private Predicate<Transaction> getPredicateForOption(SearchOption option) {
        return switch (option) {
            case SEARCH_BY_CATEGORY -> categoryPredicate();
            case SEARCH_BY_AMOUNT -> amountPredicate();
            case SEARCH_BY_COMMENT -> commentPredicate();
            case SEARCH_BY_DATES -> datePredicate();
            default -> null;
        };
    }


}

