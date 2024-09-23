package org.com.Service;

import org.com.Models.Helper.CsvCinematic;
import org.com.Models.Enums.State;
import org.com.Models.Enums.Type;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;

public class CsvImporter {
    private final LineValidator validator = new LineValidator();
    private static final String CSV_DELIMITER = ",";
    private final String filePath;
    private int lineNumber = 2; // 2 because we read the header and first time in the while loop

    public CsvImporter(String filePath) {
        this.filePath = filePath;
    }

    public List<CsvCinematic> importData() throws IOException {
        List<CsvCinematic> cinematics = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String header = br.readLine();

            if (header == null || !validator.isValidHeaderLength(header)) {
                throw new RuntimeException("Invalid CSV Header: Must contain exactly 4 columns.");
            }

            if (!validator.isValidHeader(header)) {
                throw new RuntimeException("Invalid CSV Header: Must has right Header names for his columns.");
            }

            while ((line = br.readLine()) != null) {
                String[] columns = line.split(CSV_DELIMITER);
                cinematics.add(createPersonFromCsvLine(columns));
                lineNumber++;
            }
        }

        return cinematics;
    }

    private CsvCinematic createPersonFromCsvLine(String[] columns) {
        if(!validator.isValidRecord(columns)){
            throw new RuntimeException("CSV line is Invalid at line:" + lineNumber);
        }

        if (columns.length == 3) {
            return new CsvCinematic.Builder()
                    .withTitle(columns[0])
                    .withType(parseTypes(columns[1]))
                    .withState(parseStates(columns[2]))
                    .build();
        }

        return new CsvCinematic.Builder()
                .withTitle(columns[0])
                .withType(parseTypes(columns[1]))
                .withState(parseStates(columns[2]))
                .withRating(parseStringToInt(columns[3]))
                .build();
    }

    private Type parseTypes(String typeString) {
        return switch (typeString) {
            case "MOVIE" -> Type.MOVIE;
            case "ANIME" -> Type.ANIME;
            case "SERIES" -> Type.SERIES;
            default ->
                    throw new IllegalArgumentException("Invalid Type:" + typeString + "at index:" + lineNumber +  "- valid are MOVIE,ANIME,SERIES");
        };
    }

    private State parseStates(String stateString) {
        return switch (stateString) {
            case "FINISHED" -> State.FINISHED;
            case "WATCHING" -> State.WATCHING;
            case "DROPPED" -> State.DROPPED;
            case "TOWATCH" -> State.TOWATCH;
            default ->
                    throw new IllegalArgumentException("Invalid State:" + stateString + "at index:" + lineNumber +   "- valid are FINISHED,WATCHING,DROPPED,TOWATCH");
        };
    }

    public int parseStringToInt(String input) {
        try {
            int number = Integer.parseInt(input);

            if (number < 1 || number > 10) {
                throw new IllegalArgumentException("Invalid input: " + input + "at index:" + lineNumber +   " is not between 1 and 10.");
            }

            return number;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input: " + input + "at index:" + lineNumber +   " is not a valid number.");
        }
    }
}

