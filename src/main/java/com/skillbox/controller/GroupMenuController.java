package com.skillbox.controller;

import com.skillbox.controller.dto.GroupingTransactionDto;
import com.skillbox.controller.option.GroupOption;

public class GroupMenuController extends AbstractMenuController<GroupOption> {
    public GroupMenuController() {
        super(GroupOption.class, "Выберите способ группировки транзакции");
    }

    public GroupingTransactionDto getGropingTransaction(GroupingTransactionDto filter) {
        boolean isCompleted = false;

        while (!isCompleted) {
            GroupOption option = selectMenu();
            switch (option) {
                case EXIT:
                    isCompleted = true;
                    break;
                case GROUP_BY_MONTH:
                    isCompleted = true;
                    filter = groupingByMonth(filter);
                    break;
                case GROUP_BY_YEAR:
                    filter = groupingByYear(filter);
                    isCompleted = true;
                    break;
                case GROUP_BY_DAY_OF_WEEK:
                    filter = groupingByDayOfWeek(filter);
                    isCompleted = true;
                    break;
                case GROUP_CATEGORIES:
                    filter = groupingCategories(filter);
                    isCompleted = true;
                    break;
                case COUNT_INCOME_AND_EXPENSES:
                    filter = countIncomeAndExpenses(filter);
                    isCompleted = true;
                    break;
                case GROUP_BY_ACCOUNT_TYPE:
                    filter = groupingByAccountType(filter);
                    isCompleted = true;
                    break;
                case GROUP_BY_USER_ID:
                    filter = groupingByUserID(filter);
                    isCompleted = true;
                    break;
            }
        }
        return filter;
    }

    private GroupingTransactionDto groupingByUserID(GroupingTransactionDto filter) {
        filter.setGroupOption(GroupOption.GROUP_BY_USER_ID);
        return filter;
    }

    private GroupingTransactionDto groupingByAccountType(GroupingTransactionDto filter) {
        filter.setGroupOption(GroupOption.GROUP_BY_ACCOUNT_TYPE);
        return filter;
    }

    private GroupingTransactionDto countIncomeAndExpenses(GroupingTransactionDto filter) {
        filter.setGroupOption(GroupOption.COUNT_INCOME_AND_EXPENSES);
        return filter;
    }

    private GroupingTransactionDto groupingCategories(GroupingTransactionDto filter) {
        filter.setGroupOption(GroupOption.GROUP_CATEGORIES);
        return filter;
    }

    private GroupingTransactionDto groupingByDayOfWeek(GroupingTransactionDto filter) {
        filter.setGroupOption(GroupOption.GROUP_BY_DAY_OF_WEEK);
        return filter;
    }

    private GroupingTransactionDto groupingByYear(GroupingTransactionDto filter) {
        filter.setGroupOption(GroupOption.GROUP_BY_YEAR);
        return filter;
    }

    private GroupingTransactionDto groupingByMonth(GroupingTransactionDto filter) {
        filter.setGroupOption(GroupOption.GROUP_BY_MONTH);
        return filter;
    }

}
