package junit;

import org.com.Models.Cinematic;
import org.com.Service.ApiData;
import org.com.Service.CineFactory;
import org.com.Service.CsvImporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CineFactoryTest {
    private CineFactory cineFactory;
    private CsvImporter csvImporter;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("Test", ".csv");
        csvImporter = new CsvImporter(tempFile.getAbsolutePath());
        ApiData apiData = new ApiData();
        cineFactory = new CineFactory(csvImporter,apiData);
    }

    @Test
    void testReadCSVValid() throws IOException {
        String validCSVContent = "Title,Type,State,Rating\n" +
                "The Fighter,MOVIE,FINISHED,6\n" +
                "Up,MOVIE,FINISHED,8\n" +
                "The Curious Case of Benjamin Button,MOVIE,TOWATCH\n"+
                "Oppenheimer,MOVIE,FINISHED,6\n"+
                "One Piece,ANIME,WATCHING";

        writeToFile(validCSVContent);

        List<Cinematic> cinematics = cineFactory.createCinematics();

        assertEquals(5, cinematics.size());
    }

    @Test
    void testReadCSVValidFromFile() throws IOException {


        csvImporter = new CsvImporter("C:\\Users\\umut2\\Desktop\\Programmieren\\Projekte\\CineTrack\\src\\CineTrack\\src\\test\\resources\\Test.csv");
        ApiData apiData = new ApiData();
        cineFactory = new CineFactory(csvImporter,apiData);

        List<Cinematic> cinematics = cineFactory.createCinematics();

        assertEquals(5, cinematics.size());
    }

    @Test
    void testReadCSVInvalid() throws IOException {
        String validCSVContent = "Title,Type,State,Rating\n" +
                "The Fighter,MOVIE,FINISHED,6\n" +
                "Txhe Bixg Bxang Theory,SERIES,FINISHED,8";

        writeToFile(validCSVContent);

        List<Cinematic> cinematics = cineFactory.createCinematics();

        assertEquals(1, cinematics.size());
    }

    private void writeToFile(String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(content);
        }
    }
}
