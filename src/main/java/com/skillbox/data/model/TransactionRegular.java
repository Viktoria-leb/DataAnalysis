package com.skillbox.data.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionRegular extends Transaction {
    public TransactionRegular(Integer idScoreTransaction,
                              Integer idTransaction,
                              LocalDateTime dateTransaction,
                              String categoryTransaction,
                              BigDecimal amountTransaction,
                              TypeTransaction typeTransaction) {
        super(idScoreTransaction,
                idTransaction, dateTransaction,
                categoryTransaction, amountTransaction,
                typeTransaction);
    }

    @Override
    public BigDecimal amountTransaction() {
        return getAmountTransaction();
    }

    @Override
    public String toString() {
        return "TransactionRegular: " + toStrings();
    }

}

