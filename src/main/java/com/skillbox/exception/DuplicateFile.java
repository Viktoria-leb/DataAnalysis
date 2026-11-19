package com.skillbox.exception;

import java.util.List;

public class DuplicateFile extends RuntimeException {
    public DuplicateFile() {
        super();
    }

    @Override
    public String getMessage() {
        return stringOfFile();
    }

    private String stringOfFile() {
        List<Integer> duplication = DuplicateFiles.getDuplicates();
        StringBuilder builder = new StringBuilder();
        builder.append("Присутствуют файлы с одинаковыми данными: ");
        duplication.forEach(n ->
                builder.append(CheckingNameFiles.whatIsTheFile(n)).append("\n")
                        .append("Необходимо заменить один файл \"")
                        .append(CheckingNameFiles.whatIsTheFile(n))
                        .append("\" на: "));
        CheckingNameFiles.missingFiles(DuplicateFiles
                .getFileLength(), builder);
        return builder.toString();
    }
}

