package com.skillbox.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class TransactionTaxable extends Transaction implements Taxable {
    @JsonProperty("Процент налога")
    private final Double percentageOfTax;

    public TransactionTaxable(Integer idScoreTransaction,
                              Integer idTransaction,
                              LocalDateTime dateTransaction,
                              String categoryTransaction,
                              BigDecimal amountTransaction,
                              TypeTransaction typeTransaction,
                              Double percentageOfTax) {
        super(idScoreTransaction,
                idTransaction,
                dateTransaction,
                categoryTransaction,
                amountTransaction,
                typeTransaction);
        this.percentageOfTax = percentageOfTax;
    }

    public Double getPercentageOfTax() {
        return percentageOfTax;
    }

    @Override
    public BigDecimal calculateTax() {
        BigDecimal tax = getAmountTransaction().multiply(BigDecimal.valueOf(getPercentageOfTax()));
        return tax.setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public String toString() {
        return "TransactionTaxable: " + toStrings() +
                ", percentageOfTax " + percentageOfTax;
    }

    @Override
    public BigDecimal amountTransaction() {
        return getAmountTransaction().subtract(calculateTax());
    }
}

