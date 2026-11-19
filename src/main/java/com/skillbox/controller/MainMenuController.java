package com.skillbox.controller;

import com.skillbox.controller.dto.AggregateTransactionsDto;
import com.skillbox.controller.dto.GroupingTransactionDto;
import com.skillbox.controller.dto.TransactionFilterDto;
import com.skillbox.controller.option.MainMenuOption;
import com.skillbox.data.model.Analytic;
import com.skillbox.data.repository.AnalyticRepository;
import com.skillbox.service.ServiceTransaction;


/**
 * Консольный контроллер для управления навигацией по главному меню.
 */
public class MainMenuController extends AbstractMenuController<MainMenuOption> {

    private final ServiceTransaction serviceTransaction;
    private final AnalyticRepository saver;
    private final SearchMenuController searchMenuController;
    private final GroupMenuController groupMenuController;
    private final AggregateMenuController aggregateMenuController;

    public MainMenuController(ServiceTransaction transactionService, AnalyticRepository saver) {
        super(MainMenuOption.class, "Анализ финансов");
        this.serviceTransaction = transactionService;
        this.saver = saver;
        this.searchMenuController = new SearchMenuController();
        this.aggregateMenuController = new AggregateMenuController();
        this.groupMenuController = new GroupMenuController();
    }

    public void start() {
        goMainMenu();
    }

    private void goMainMenu() {
        TransactionFilterDto transactionFilter = new TransactionFilterDto();
        Analytic analytics = null;
        GroupingTransactionDto groupingTransaction = new GroupingTransactionDto();
        AggregateTransactionsDto aggregateTransactions = new AggregateTransactionsDto();

        while (true) {
            MainMenuOption i = selectMenu();
            switch (i) {
                case SEARCH_CRITERIA:
                    transactionFilter = searchMenuController.getTransactionFilter(transactionFilter);
                    break;
                case GROUP_OPTION:
                    groupingTransaction =groupMenuController.getGropingTransaction(groupingTransaction);
                    break;
                case AGGREGATION_METHOD:
                    aggregateTransactions = aggregateMenuController.getAggregateTransaction(aggregateTransactions);
                    break;
                case CALCULATE_ANALYTICS:
                    analytics = serviceTransaction.calculateAnalytics(transactionFilter,
                            groupingTransaction, aggregateTransactions);
                    System.out.println(analytics);
                    break;
                case SAVE_ANALYTICS:
                    if (analytics == null) {
                        System.err.println("Необходимо сначала рассчитать аналитику");
                        break;
                    }
                    saver.save(analytics);
                    break;
                case EXIT:
                    return;
            }
        }
    }

}
