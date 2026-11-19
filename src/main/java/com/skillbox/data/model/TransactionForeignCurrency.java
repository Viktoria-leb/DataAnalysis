package com.skillbox.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class TransactionForeignCurrency extends Transaction implements CurrencyConvertible {
    @JsonProperty("Курс иностранной валюты")
    private final BigDecimal currentExchangeRate;

    public TransactionForeignCurrency(Integer idScoreTransaction,
                                      Integer idTransaction,
                                      LocalDateTime dateTransaction,
                                      String categoryTransaction,
                                      BigDecimal amountTransaction,
                                      TypeTransaction typeTransaction,
                                      BigDecimal currentExchangeRate) {
        super(idScoreTransaction,
                idTransaction,
                dateTransaction,
                categoryTransaction,
                amountTransaction,
                typeTransaction);
        this.currentExchangeRate = currentExchangeRate;
    }

    public BigDecimal getCurrentExchangeRate() {
        return currentExchangeRate;
    }

    @Override
    public BigDecimal convertToBaseCurrency(BigDecimal amount) {
        return amount.
                multiply(getCurrentExchangeRate())
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public String toString() {
        return "TransactionForeignCurrency: " + toStrings() +
                ", currentExchangeRate " + currentExchangeRate;
    }

    @Override
    public BigDecimal amountTransaction() {
        return convertToBaseCurrency(getAmountTransaction());
    }
}

