package com.skillbox.controller;

import com.skillbox.controller.dto.TransactionFilterDto;
import com.skillbox.controller.option.SearchOption;
import com.skillbox.exception.DuplicateOptionWhenSearching;
import com.skillbox.exception.IncorrectSearchRange;
import com.skillbox.exception.SearchByOnlyOneCriterion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.concurrent.TimeUnit;

/**
 * Консольный контроллер для управления навигацией по функционалу поиска транзакций.
 */
public class SearchMenuController extends AbstractMenuController<SearchOption> {

    public SearchMenuController() {
        super(SearchOption.class,
                "Выберите способ поиска транзакции");
    }

    public TransactionFilterDto getTransactionFilter(TransactionFilterDto filter) {
        while (true) {
            SearchOption option = selectMenu();
            switch (option) {
                case EXIT:
                    return filter;
                case ALL_TRANSACTION:
                    filter.setOptions();
                    break;
                case SEARCH_BY_CATEGORY:
                    try {
                        filter.addOption(SearchOption.SEARCH_BY_CATEGORY);
                        inputCategory(filter);
                    } catch (DuplicateOptionWhenSearching e) {
                        System.err.println(e.getMessage());
                        sleep();
                        filter.addOption(SearchOption.SEARCH_BY_CATEGORY);
                        inputCategory(filter);
                    }
                    break;

                case SEARCH_BY_DATES:
                    try {
                        filter.addOption(SearchOption.SEARCH_BY_DATES);
                        inputDates(filter);
                    } catch (SearchByOnlyOneCriterion e) {
                        System.err.println(e.getMessage());
                    } catch (IncorrectSearchRange e) {
                        System.err.println(e.getMessage());
                        inputDates(filter);
                    } catch (DuplicateOptionWhenSearching e) {
                        System.err.println(e.getMessage());
                        sleep();
                        filter.addOption(SearchOption.SEARCH_BY_DATES);
                        inputDates(filter);
                    }
                    break;

                case SEARCH_BY_AMOUNT:
                    try {
                        filter.addOption(SearchOption.SEARCH_BY_AMOUNT);
                        inputAmount(filter);
                    } catch (SearchByOnlyOneCriterion e) {
                        System.err.println(e.getMessage());
                    } catch (IncorrectSearchRange e) {
                        System.err.println(e.getMessage());
                        inputAmount(filter);
                    } catch (DuplicateOptionWhenSearching e) {
                        System.err.println(e.getMessage());
                        sleep();
                        filter.addOption(SearchOption.SEARCH_BY_AMOUNT);
                        inputAmount(filter);
                    }
                    break;

                case SEARCH_BY_COMMENT:
                    try {
                        filter.addOption(SearchOption.SEARCH_BY_COMMENT);
                        inputComment(filter);
                    } catch (DuplicateOptionWhenSearching e) {
                        System.err.println(e.getMessage());
                        sleep();
                        filter.addOption(SearchOption.SEARCH_BY_COMMENT);
                        inputComment(filter);
                    }
                    break;
            }
        }
    }

    private void sleep() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void inputComment(TransactionFilterDto filter) {
        System.out.println("Введите комментарий или его часть" +
                "(Enter -  для поиска всех комментариев");
        String input = scanner.next().trim();
        if (input.isEmpty()) {
            filter.setComment(null);
        } else {
            filter.setComment(input);
        }
    }

    private void inputAmount(TransactionFilterDto filter) {
        BigDecimal start;
        BigDecimal end;
        String information = "Введите минимальную сумму транзакции" +
                " (Enter - не ограничивать минимальную сумму)\n";
        System.out.print(information);
        String inputStart = scanner.next().trim();
        start = inputStringBigDecimal(information, inputStart);

        information = "Введите максимальную сумму транзакции " +
                "(Enter - не ограничивать максимальную сумму)\n";
        System.out.print(information);
        String inputEnd = scanner.next().trim();
        end = inputStringBigDecimal(information, inputEnd);
        filter.setStartAmount(start);
        filter.setEndAmount(end);
        if (start != null && end != null) {
            if (end.compareTo(start) < 0) {
                throw new IncorrectSearchRange();
            } else if (end.compareTo(start) == 0) {
                throw new SearchByOnlyOneCriterion("Поиск только по одной сумме: " + end);
            }
        }
    }

    private BigDecimal inputStringBigDecimal(String information,
                                             String input) {
        if (!input.isEmpty()) {
            try {
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.err.println("Ошибка: введено некорректное число." +
                        " Сумма транзакций не ограничена.");
                System.out.println(information);
                input = scanner.next().trim();
                return inputStringBigDecimal(information, input);
            }
        }
        return null;
    }

    private void inputDates(TransactionFilterDto filter) {
        LocalDate start;
        LocalDate end;
        String information = "Введите начальную дату транзакции" +
                "(Формат: ГГГГ-ММ-ДД, Enter - не ограничивать начальную дату)";
        System.out.println(information);
        String inputStart = scanner.next().trim();
        start = inputStringLocalDate(information, inputStart);
        information = "Введите конечную дату транзакции" +
                "(Формат: ГГГГ-ММ-ДД, Enter - не ограничивать начальную дату)";
        System.out.println(information);
        String inputEnd = scanner.next().trim();
        end = inputStringLocalDate(information, inputEnd);
        filter.setStartDate(start);
        filter.setEndDate(end);
        if (start != null && end != null) {
            if (end.isBefore(start)) {
                throw new IncorrectSearchRange();
            } else if (end.compareTo(start) == 0) {
                throw new SearchByOnlyOneCriterion("Поиск только по одной дате: " + end);
            }
        }
    }

    private LocalDate inputStringLocalDate(String information, String input) {
        if (!input.isEmpty()) {
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.err.println("Ошибка: Неверный формат даты." +
                        "Дата не ограничена.");
                System.out.println(information);
                input = scanner.next().trim();
                return inputStringLocalDate(information, input);
            }
        }
        return null;
    }

    private void inputCategory(TransactionFilterDto filter) {
        System.out.println("Введите категорию(Enter - поиск по всем категориям)");
        String category = scanner.next().trim();
        if (category.isEmpty()) {
            filter.setCategory(null);
        } else {
            filter.setCategory(category);
        }
    }

}

