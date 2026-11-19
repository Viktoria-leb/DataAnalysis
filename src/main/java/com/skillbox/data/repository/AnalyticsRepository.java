package com.skillbox.data.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.skillbox.data.model.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class AnalyticsRepository implements AnalyticRepository{
    private final String fileName;
    private String jsonAnalytic;

    public AnalyticsRepository(String fileName) {
        this.fileName = fileName;
        jsonAnalytic = "";
    }
    public String getFileName() {
        return fileName;
    }

    @Override
    public void save(Analytic analytic) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            jsonAnalytic = mapper.writeValueAsString(analytic);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        bufferedWriteStringToFile(jsonAnalytic);
    }

    private void bufferedWriteStringToFile(String jsonAnalytic) {

        Path filePath =Paths.get(getFileName());
        StringBuilder builder = new StringBuilder();
        int open = 0;
        for (int i=0; i<jsonAnalytic.length(); i++){
            char charI = jsonAnalytic.charAt(i);
            if(charI==','){
                builder.append(charI);
              lineBreakAndSpaces(builder, open);
            }
            else if(charI=='{') {
                open += 1;
                builder.append(charI);
                lineBreakAndSpaces(builder, open);
            }
            else if(jsonAnalytic.charAt(i)=='}'){

               lineBreakAndSpaces(builder, open);
               open-=1;
               builder.append(charI);
            }else {builder.append(charI);}
      }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            writer.write(builder.toString());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void lineBreakAndSpaces(StringBuilder builder, int open){
        builder.append("\n");
        if(open>1){
            builder.append("  ".repeat(open - 1));
        }}
}

