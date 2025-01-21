package junit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.com.model.domain.CsvCinematic;
import org.com.service.ApiService;
import org.com.service.CineFactoryService;
import org.com.service.CsvImporterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ApiServiceTest {

  private CineFactoryService cineFactoryService;
  private CsvImporterService csvImporterService;
  private File tempFile;

  @BeforeEach
  void setUp() throws IOException {
    tempFile = File.createTempFile("Test", ".csv");
    csvImporterService = new CsvImporterService(tempFile.getAbsolutePath());
  }

  @Test
  void testReadCSVValid() throws IOException {
    String validCSVContent = "Title,Type,State,Rating\n" +
        "The Fighter,MOVIE,FINISHED,6\n" +
        "Up,MOVIE,FINISHED,8\n" +
        "The Curious Case of Benjamin Button,MOVIE,TOWATCH\n" +
        "Oppenheimer,MOVIE,FINISHED,6\n";

    writeToFile(validCSVContent);

    List<CsvCinematic> cinematics = csvImporterService.importData();
    ApiService apiService = new ApiService();
    for (CsvCinematic csvCinematic : cinematics) {
      apiService.fetchMoviesOrSeries(csvCinematic.getTitle());
    }
  }

  private void writeToFile(String content) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
      writer.write(content);
    }
  }
}
