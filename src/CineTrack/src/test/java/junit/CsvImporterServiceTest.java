package junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.com.model.domain.CsvCinematic;
import org.com.service.CsvImporterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CsvImporterServiceTest {

  private CsvImporterService csvImporterService;
  private File tempFile;

  @BeforeEach
  void setUp() throws IOException {
    tempFile = File.createTempFile("Test", ".csv");
    csvImporterService = new CsvImporterService(tempFile.getAbsolutePath());
  }

  @Test
  void testImportDataValid() throws IOException {
    String validCSVContent = """
        Title,Type,State,Rating
        The Fighter,MOVIE,FINISHED,1
        Up,SERIES,TOWATCH
        Vice,MOVIE,DROPPED,8
        """;

    writeToFile(validCSVContent);

    List<CsvCinematic> cinematics = csvImporterService.importData();

    assertEquals(3, cinematics.size());
    assertEquals(List.of("The Fighter", "MOVIE", "FINISHED", 1),
        List.of(cinematics.getFirst().getTitle(), cinematics.getFirst().getType().toString(),
            cinematics.getFirst().getState().toString(), cinematics.getFirst().getRating()));
    assertEquals(List.of("Up", "SERIES", "TOWATCH"),
        List.of(cinematics.get(1).getTitle(), cinematics.get(1).getType().toString(),
            cinematics.get(1).getState().toString()));
    assertEquals(List.of("Vice", "MOVIE", "DROPPED"),
        List.of(cinematics.get(2).getTitle(), cinematics.get(2).getType().toString(),
            cinematics.get(2).getState().toString()));
  }

  @Test
  void testImportDataInvalid() throws IOException {
    String invalidCSVContent = """
        tooFew
        justTwo,columns
        valid1,valid2,valid3
        """;

    writeToFile(invalidCSVContent);

    assertThrows(RuntimeException.class, () -> csvImporterService.importData());
  }

  // help-method that I can write some csv content into a file
  private void writeToFile(String content) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
      writer.write(content);
    }
  }
}