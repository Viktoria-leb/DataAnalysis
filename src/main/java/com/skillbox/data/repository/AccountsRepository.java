package com.skillbox.data.repository;

import com.skillbox.FileSearch;
import com.skillbox.data.model.Account;
import com.skillbox.data.model.AccountType;
import com.skillbox.exception.IncorrectFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AccountsRepository implements AccountRepository {

    public static List<String> readingAccountFile(String accountFilename) {
        String path = FileSearch.fileSearch(accountFilename);
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return lines;
    }

    @Override
    public List<Account> readAll(String accountFilename) {
        List<Account> accountList = new ArrayList<>();
        for (String line : readingAccountFile(accountFilename)) {
            String[] fragments = line.split(",");
            if (fragments.length != 3) {
                throw new IncorrectFile("Проверьте, чтоб " +
                        "на первом месте располагался файл с" +
                        "данными о счетах");
            }
            accountList.add(new Account(Integer.parseInt(fragments[0]),
                    AccountType.of(Integer.parseInt(fragments[1])),
                    Integer.parseInt(fragments[2])));
        }
        return accountList;
    }
}

