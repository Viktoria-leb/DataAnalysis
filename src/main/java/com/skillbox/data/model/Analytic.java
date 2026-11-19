package com.skillbox.data.model;

import com.fasterxml.jackson.annotation.*;
import com.skillbox.controller.dto.TransactionFilterDto;
import com.skillbox.controller.option.SearchOption;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, хранящий результаты расчета аналитики транзакций
 */
public abstract class Analytic {
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonProperty("Дата и Время")
    protected LocalDateTime dateTime;
    @JsonIgnore
    protected List<SearchOption> searchOption;
    protected TransactionFilterDto transactionFilter;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("Опции фильтрации")
    private List<String> options;

    public Analytic(LocalDateTime dateTime,
                    List<SearchOption> searchOption,
                    TransactionFilterDto transactionFilter) {
        this.dateTime = dateTime;
        this.searchOption = searchOption;
        this.transactionFilter = transactionFilter;
        options = new ArrayList<>();
    }

    public List<String> getOptions() {
        for (SearchOption search: searchOption) {
            if (search.equals(SearchOption.SEARCH_BY_AMOUNT)) {
                String start = String.valueOf(transactionFilter.getStartAmount() != null
                        ? transactionFilter.getStartAmount() : "без ограничения");
                String end = String.valueOf(transactionFilter.getEndAmount() != null
                        ? transactionFilter.getEndAmount() : "без ограничения");
                options.add(search.getName() +  ": Начальная сумма: " + start +
                        " Конечная сумма: " + end);
            } else if (search.equals(SearchOption.SEARCH_BY_DATES)) {
                LocalDate start = transactionFilter.getStartDate();
                LocalDate end = transactionFilter.getEndDate();
                options.add(search.getName()+
                        ": с "+ (start != null ? start : "любой даты")+
                        " по "+(end != null ? end : "текущую дату"));
            } else if (search.equals(SearchOption.SEARCH_BY_CATEGORY)) {
                String category = transactionFilter.getCategory();
                options.add(search.getName()+ ": "
                                +category);
            } else if (search.equals(SearchOption.SEARCH_BY_COMMENT)) {
                String comment = transactionFilter.getComment();
                options.add(search.getName()+": "
                               + comment);
            }
        }
        return options;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        StringBuilder builder = new StringBuilder();
        builder.append("============================================================")
                .append("\n");
        builder.append(String.format("%-35s %-35s", "Дата: ", dateTime.format(formatter)))
                .append("\n");
         if (!searchOption.isEmpty()) {
            builder.append(String.format("%-35s", "Фильтрации: ")).append("\n");
            builder.append(toStringSearchOption(searchOption, transactionFilter));
        }
         return builder.toString();
    }

    public static String toStringSearchOption(List<SearchOption> searchOption, TransactionFilterDto transactionFilter) {
        StringBuilder builder = new StringBuilder();
        for (SearchOption search : searchOption) {
            if (search.equals(SearchOption.SEARCH_BY_AMOUNT)) {
                String start = String.valueOf(transactionFilter.getStartAmount() != null
                        ? transactionFilter.getStartAmount() : "без ограничения");
                String end = String.valueOf(transactionFilter.getEndAmount() != null
                        ? transactionFilter.getEndAmount() : "без ограничения");
                builder.append(String.format("%35s %-35s %-20s %-15s %-20s %-15s ", "",
                        search.getName(), "Начальная сумма: ", start,
                        "Конечная сумма: ", end)).append("\n");
            } else if (search.equals(SearchOption.SEARCH_BY_DATES)) {
                LocalDate start = transactionFilter.getStartDate();
                LocalDate end = transactionFilter.getEndDate();
                builder.append(String.format("%35s %-35s %-10s %-25s %-10s %-25s ", "", search.getName(),
                        "с ", (start != null ? start : "любой даты"),
                        " по ", (end != null ? end : "текущую дату"))).append("\n");
            } else if (search.equals(SearchOption.SEARCH_BY_CATEGORY)) {
                String category = transactionFilter.getCategory();
                builder.append(String.format("%35s %-35s %-35s", "", search.getName(),
                                category))
                        .append("\n");
            } else if (search.equals(SearchOption.SEARCH_BY_COMMENT)) {
                String comment = transactionFilter.getComment();
                builder.append(String.format("%35s %-35s %-35s", "", search.getName(),
                                comment))
                        .append("\n");
            }
        }
        return builder.toString();
    }

}
