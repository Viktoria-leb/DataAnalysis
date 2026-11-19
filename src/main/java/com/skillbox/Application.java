package com.skillbox;


import com.skillbox.controller.MainMenuController;
import com.skillbox.data.model.*;
import com.skillbox.data.repository.*;
import com.skillbox.exception.*;
import com.skillbox.service.ServiceTransaction;
import java.util.*;
import java.util.stream.Collectors;

public class Application {

    public static void main(String[] args) {
       try{
            if (args.length != 3) {
                throw new LackOfResources();
            } else if(!DuplicateFiles.duplicateFiles(args)){
                throw new DuplicateFile();
            }
        String accountFilename = args[0];
        String transactionFilename = args[1];
        String outputFilename = args[2];

        AccountRepository accountReader = new AccountsRepository();
        List<Account> accounts = accountReader.readAll(accountFilename);
        TransactionRepository transactionReader = new TransactionsRepository();
        transactionReader.readAll(transactionFilename, accounts);
        List<Transaction> transactions = accounts.stream()
                .flatMap(account -> account.getTransactions().stream())
                .collect(Collectors.toList());
           ServiceTransaction transactionService =
                new ServiceTransaction(accounts, transactions);
        AnalyticRepository saver = new AnalyticsRepository(outputFilename);
        new MainMenuController(transactionService, saver).start();
    }
    catch (DuplicateFile e){
            System.err.println(e.getMessage());
    } catch (LackOfResources z){
           System.err.println(z.getMessage(args));
       }
}
}




