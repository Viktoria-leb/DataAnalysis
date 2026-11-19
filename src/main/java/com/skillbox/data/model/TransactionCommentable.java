package com.skillbox.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionCommentable extends Transaction implements Commentable {
    @JsonProperty("Комментарий")
    private final List<String> comment;

    public TransactionCommentable(Integer idScoreTransaction,
                                  Integer idTransaction,
                                  LocalDateTime dateTransaction,
                                  String categoryTransaction,
                                  BigDecimal amountTransaction,
                                  TypeTransaction typeTransaction,
                                  List<String> comment) {
        super(idScoreTransaction,
                idTransaction,
                dateTransaction,
                categoryTransaction,
                amountTransaction,
                typeTransaction);
        this.comment = comment;
    }

    public List<String> getComment() {
        return comment;
    }

    @Override
    public List<String> getComments() {
        return getComment();
    }

    @Override
    public String toString() {
        return "TransactionCommentable: " + toStrings() +
                ", comment " + comment;
    }

    @Override
    public BigDecimal amountTransaction() {
        return getAmountTransaction();
    }
}

