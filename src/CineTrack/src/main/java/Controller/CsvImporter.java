package Controller;

import Models.CsvCinematic;
import Models.State;
import Models.Type;

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
                    throw new IllegalArgumentException("Invalid Type:" + typeString + "- valid are MOVIE,ANIME,SERIES");
        };
    }

    private State parseStates(String stateString) {
        return switch (stateString) {
            case "FINISHED" -> State.FINISHED;
            case "WATCHING" -> State.WATCHING;
            case "DROPPED" -> State.DROPPED;
            case "TOWATCH" -> State.TOWATCH;
            default ->
                    throw new IllegalArgumentException("Invalid State:" + stateString + "- valid are FINISHED,WATCHING,DROPPED,TOWATCH");
        };
    }

    public int parseStringToInt(String input) {
        try {
            int number = Integer.parseInt(input);

            if (number < 1 || number > 10) {
                throw new IllegalArgumentException("Invalid input: " + input + " is not between 1 and 10.");
            }

            return number;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input: " + input + " is not a valid number.");
        }
    }
}

class LineValidator {
    private static final int HEADER_COLUMNS = 4;
    private static final int MIN_COLUMNS = 3;
    private static final int MAX_COLUMNS = 4;

    public boolean isValidHeaderLength(String header) {
        String[] columns = header.split(",");
        return columns.length == HEADER_COLUMNS;
    }

    public boolean isValidHeader(String header) {
        String[] columns = header.split(",");
        return "Title".equalsIgnoreCase(columns[0]) && "Type".equalsIgnoreCase(columns[1]) && "State".equalsIgnoreCase(columns[2]) && "Rating".equalsIgnoreCase(columns[3]);
    }

    public boolean isValidRecord(String[] columns) {
        int size = columns.length;

        if (size < MIN_COLUMNS || size > MAX_COLUMNS) {
            return false;
        }

        if (columns[0].isBlank() || columns[1].isBlank() || columns[2].isBlank()) {
            return false;
        }

        return size != MAX_COLUMNS || !columns[3].isBlank();
    }
}
