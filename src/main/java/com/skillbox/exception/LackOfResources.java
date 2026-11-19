package com.skillbox.exception;

import com.skillbox.FileSearch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LackOfResources extends RuntimeException {
    public LackOfResources() {
        super();
    }

    public String getMessage(String[] args) {
        return stringOfFile(args);
    }

    private String stringOfFile(String[] args) {
        StringBuilder builder = new StringBuilder();
        if (args.length < 3) {
            List<Integer> fileLength = new ArrayList<>();
            builder = new StringBuilder();
            builder.append("Присутствует: ").append("\n");
            for (String arg : args) {
                String path = FileSearch.fileSearch(arg);
                File file = new File(path);
                String files = "";
                if (file.exists() && file.isFile()) {
                    try {
                        files = CheckingNameFiles.whatIsTheFile(CheckingNameFiles.readingFile(path));
                        fileLength.add(CheckingNameFiles.readingFile(path));
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                } else {
                    fileLength.add(0);
                    files = CheckingNameFiles.whatIsTheFile(0);
                }
                builder.append(files).append(" - ").append(arg).append("\n");
            }
            builder.append("Необходимо добавить: ").append("\n");

            CheckingNameFiles.missingFiles(fileLength, builder);
        } else if (args.length > 3) {
            builder = new StringBuilder();
            builder.append("Добавлено слишком много файлов");
        }
        return builder.toString();
    }
}

