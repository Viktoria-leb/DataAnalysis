package com.skillbox.data.repository;

import com.skillbox.FileSearch;
import com.skillbox.data.model.Account;
import com.skillbox.data.model.Transaction;
import com.skillbox.data.model.TransactionFactory;
import com.skillbox.exception.IncorrectFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransactionsRepository implements TransactionRepository {

    public static List<String> readingTransactionFile(String transactionFilename) {
        String path = FileSearch.fileSearch(transactionFilename);
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return lines;
    }

    @Override
    public List<Transaction> readAll(String transactionFilename, List<Account> accounts) {
        List<Transaction> transactionList = new ArrayList<>();
        for (String line : readingTransactionFile(transactionFilename)) {
            String[] fragments = line.split(",");
            if (fragments.length != 6 && fragments.length != 7) {
                throw new IncorrectFile("Проверьте, чтоб " +
                        "вторым располагался файл с" +
                        "данными о транзакциях");
            }
            if (fragments.length == 6) {
                String[] fragments2 = Arrays.copyOf(fragments,
                        fragments.length + 1);
                Transaction transaction = TransactionFactory.createTransaction
                        (fragments2[0], fragments2[1], fragments2[2], fragments2[3],
                                fragments2[4], fragments2[5], fragments2[6]);
                transactionList.add(transaction);
                addTransactionInAccount(fragments2[0], accounts, transaction);
            } else {
                Transaction transaction = TransactionFactory.createTransaction
                        (fragments[0], fragments[1], fragments[2], fragments[3],
                                fragments[4], fragments[5], fragments[6]);
                transactionList.add(transaction);
                addTransactionInAccount(fragments[0], accounts, transaction);
            }
        }
        return transactionList;
    }

    private static void addTransactionInAccount(String idScope, List<Account> accounts, Transaction transaction) {
        for (Account account : accounts) {
            if (Integer.parseInt(idScope) == account.getIdScore() &&
                    account.getIdScore().equals(transaction.getIdScoreTransaction())) {
                account.addTransaction(transaction);
            }
        }
    }
}

