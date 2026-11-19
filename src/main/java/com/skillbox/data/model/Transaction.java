package com.skillbox.data.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class Transaction {
    @JsonProperty("ID счета ")
    protected Integer idScoreTransaction;
    @JsonProperty("ID ")
    protected Integer idTransaction;
    @JsonProperty("Дата и время")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    protected LocalDateTime dateTransaction;
    @JsonProperty("Категория")
    protected String categoryTransaction;
    // @JsonProperty("Сумма")
    protected BigDecimal amountTransaction;
    @JsonProperty("Тип")
    protected TypeTransaction typeTransaction;

    public Transaction(Integer idScoreTransaction,
                       Integer idTransaction,
                       LocalDateTime dateTransaction,
                       String categoryTransaction,
                       BigDecimal amountTransaction,
                       TypeTransaction typeTransaction) {
        this.idScoreTransaction = idScoreTransaction;
        this.idTransaction = idTransaction;
        this.dateTransaction = dateTransaction;
        this.categoryTransaction = categoryTransaction;
        this.amountTransaction = amountTransaction;
        this.typeTransaction = typeTransaction;
    }

    public Integer getIdScoreTransaction() {
        return idScoreTransaction;
    }

    public Integer getIdTransaction() {
        return idTransaction;
    }

    public LocalDateTime getDateTransaction() {
        return dateTransaction;
    }

    public String getCategoryTransaction() {
        return categoryTransaction;
    }

    public BigDecimal getAmountTransaction() {
        return amountTransaction;
    }

    public TypeTransaction getTypeTransaction() {
        return typeTransaction;
    }

    @JsonProperty("Сумма")
    public abstract BigDecimal amountTransaction();

    public String toStrings() {
        return
                " id " + idScoreTransaction +
                        ", idTransaction " + idTransaction +
                        ", date " + dateTransaction +
                        ", category " + categoryTransaction +
                        ", amount " + amountTransaction() +
                        ", type " + typeTransaction;
    }
}

