package com.skillbox;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class FileSearch {

    public static String fileSearch(String fileName) {
        Path path = Paths.get(fileName);
        Path searchRoot = Paths.get(".");
        final String[] pathFile = {""};
        try {
            Files.walkFileTree(searchRoot, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.getFileName().toString().equals(fileName)) {
                        pathFile[0] = file.toAbsolutePath().toString();
                        return FileVisitResult.TERMINATE;
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pathFile[0];
    }
}
