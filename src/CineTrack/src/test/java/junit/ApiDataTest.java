package junit;

import Service.ApiData;
import Service.CSVReader;
import Service.CineFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiDataTest {
    private CineFactory cineFactory;
    private CSVReader csvReader;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
       csvReader = new CSVReader();
        tempFile = File.createTempFile("Test", ".csv");
    }

    @Test
    void testReadCSVValid() throws IOException {
        String validCSVContent = "Name,Type,State,Value\n" +
                "The%20Fighter,MOVIE,FINISHED,6\n" +
                "Up,MOVIE,FINISHED,8\n" +
                "Benjamin%20Button,MOVIE,TOWATCH\n"+
                "Oppenheimer,MOVIE,FINISHED,6\n";

        writeToFile(validCSVContent);

        var csvLines = csvReader.readCSV(tempFile.getAbsolutePath());
        ApiData apiData = new ApiData(csvLines);
        apiData.fillMoviesAndSeries();
    }

    private void writeToFile(String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(content);
        }
    }
}
