package junit;

import org.com.Service.CsvImporter;
import org.com.Models.Helper.CsvCinematic;
import org.com.Service.ApiData;
import org.com.Service.CineFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ApiDataTest {
    private CineFactory cineFactory;
    private CsvImporter csvImporter;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("Test", ".csv");
        csvImporter = new CsvImporter(tempFile.getAbsolutePath());
    }

    @Test
    void testReadCSVValid() throws IOException {
        String validCSVContent = "Title,Type,State,Rating\n" +
                "The Fighter,MOVIE,FINISHED,6\n" +
                "Up,MOVIE,FINISHED,8\n" +
                "The Curious Case of Benjamin Button,MOVIE,TOWATCH\n"+
                "Oppenheimer,MOVIE,FINISHED,6\n";

        writeToFile(validCSVContent);

        List<CsvCinematic> cinematics = csvImporter.importData();
        ApiData apiData = new ApiData();
        for(CsvCinematic csvCinematic : cinematics) {
            apiData.fetchMoviesOrSeries(csvCinematic.getTitle());
        }
    }

    private void writeToFile(String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(content);
        }
    }
}
