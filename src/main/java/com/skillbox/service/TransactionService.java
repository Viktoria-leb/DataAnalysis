package com.skillbox.service;

import com.skillbox.controller.AggregateMenuController;
import com.skillbox.controller.dto.AggregateTransactionsDto;
import com.skillbox.controller.dto.GroupingTransactionDto;
import com.skillbox.controller.option.AggregateOption;
import com.skillbox.controller.option.GroupOption;
import com.skillbox.controller.dto.TransactionFilterDto;
import com.skillbox.data.model.Analytic;

/**
 * Интерфейс для обработки транзакций и расчета аналитических данных.
 */
public interface TransactionService {

    /**
     * Вычисляет аналитические данные для заданных транзакций.
     *
     * @param transactionFilter объект фильтрации транзакций, содержащий параметры фильтра.
     * @param groupingFilter     объект для группировки транзакций.
     * @param  aggregateFilter  объект для агрегирования данных.
     * @return объект {@link Analytic}, содержащий результаты вычислений.
   //  * @throws IllegalArgumentException если любой из параметров равен null.
     */
    Analytic calculateAnalytics(TransactionFilterDto transactionFilter,
                                GroupingTransactionDto groupingFilter,
                                AggregateTransactionsDto aggregateFilter
   );
}

