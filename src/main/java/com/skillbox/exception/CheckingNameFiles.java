package com.skillbox.exception;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CheckingNameFiles {

    protected static int readingFile(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = reader.readLine();
        String[] fragment = line.split(",");
        return fragment.length;
    }

    protected static String whatIsTheFile(int lineLength) {
        return switch (lineLength) {
            case 0 -> "Выходной файл";
            case 3 -> "Входной файл данных аккаунтов с данными о счетах";
            case 6 -> "Входной файл данных транзакций с данными о транзакциях";
            case 7 -> "Входной файл данных транзакций с данными о транзакциях ";
            default -> "Не верный файл";
        };
    }

    protected static void missingFiles(List<Integer> fileLength, StringBuilder builder) {
        if (!fileLength.contains(0)) {
            builder.append(whatIsTheFile(0)).append("\n");
        }
        if (!fileLength.contains(3)) {
            builder.append(whatIsTheFile(3)).append("\n");
        }
        if (!fileLength.contains(6) &&
                !fileLength.contains(7)) {
            builder.append(whatIsTheFile(6)).append("\n");
        }

    }
}

