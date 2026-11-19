package com.skillbox.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс представляющий собой счет в банке
 */
public class Account implements AccountInfo, BalanceOperations, AccountStatement {
    @JsonProperty("ID счета")
    private final Integer idScore;
    @JsonProperty("Тип счета")
    private final AccountType accountType;
    @JsonProperty("ID пользователя")
    private final Integer idUser;
    @JsonProperty("Список транзакций")
    private final List<Transaction> transactions;

    public Account(Integer idScore, AccountType accountType, Integer idUser) {
        this.idScore = idScore;
        this.accountType = accountType;
        this.idUser = idUser;
        transactions = new ArrayList<>();
    }


    public Integer getIdScore() {
        return idScore;
    }

    public Integer getIdUser() {
        return idUser;
    }

    @Override
    public int getAccountId() {
        return getIdScore();
    }

    @Override
    public int getUserId() {
        return getIdUser();
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.of(accountType.getType());
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public BigDecimal getBalance() {
        BigDecimal balance = new BigDecimal("0.0");
        for (Transaction transaction : getTransactions()) {
            BigDecimal sum = balance.add(transaction.amountTransaction());
            balance = sum.setScale(2, RoundingMode.HALF_EVEN);
        }
        return balance;
    }

    @Override
    public void addTransaction(Transaction transaction) {
        if(!transaction.getTypeTransaction().equals(TypeTransaction.Recurrent)){
        transactions.add(transaction);}
        else {
            transactions.addAll(((TransactionRecurrent) transaction).getListSeries());
        }
    }

    @Override
    public String toString() {
        return "Account: " +
                "idScore=" + idScore +
                ", accountType=" + accountType +
                ", idUser=" + idUser +
                ", List " + transactions;
    }

}
