package com.skillbox.exception;

import com.skillbox.FileSearch;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DuplicateFiles {
    private static final List<Integer> duplicates;
    private static List<Integer> fileLength;

    static {
        duplicates = new ArrayList<>();
        fileLength = new ArrayList<>();
    }

    public static boolean duplicateFiles(String[] args) {
        for (String arg : args) {
            String path = FileSearch.fileSearch(arg);
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                int length = 0;
                try {
                    length = CheckingNameFiles.readingFile(path);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
                fileLength.add(length);
            } else {
                fileLength.add(0);
            }
        }
        Set<Integer> set2 = new HashSet<>();
        fileLength.forEach(n -> {
            if (!set2.add(n)) {
                duplicates.add(n);
            }
        });
        fileLength = new ArrayList<>();
        fileLength.addAll(set2);
        return duplicates.isEmpty();
    }

    protected static List<Integer> getDuplicates() {
        return duplicates;
    }

    protected static List<Integer> getFileLength() {
        return fileLength;
    }
}

