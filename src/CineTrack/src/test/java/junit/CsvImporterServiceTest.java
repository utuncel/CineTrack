package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.com.model.domain.CsvCinematic;
import org.com.model.enums.State;
import org.com.model.enums.Type;
import org.com.service.CsvImporterService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class CsvImporterServiceTest extends JavaFXTestBase {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void testImportData_Success_WithRating() throws IOException {
    File csvFile = folder.newFile("cinematics_with_rating.csv");
    FileWriter writer = new FileWriter(csvFile);
    writer.write("title,type,state,rating\n");
    writer.write("Inception,MOVIE,FINISHED,5\n");
    writer.close();

    CsvImporterService importer = new CsvImporterService(csvFile.getAbsolutePath());
    List<CsvCinematic> result = importer.importData();

    assertNotNull(result);
    assertEquals(1, result.size());
    CsvCinematic cinematic = result.get(0);
    assertEquals("Inception", cinematic.getTitle());
    assertEquals(5, cinematic.getRating());
    assertEquals(State.FINISHED, cinematic.getState());
    assertEquals(Type.MOVIE, cinematic.getType());
  }


  @Test
  public void testImportData_Success_WithoutRating() throws IOException {
    File csvFile = folder.newFile("cinematics_without_rating.csv");
    FileWriter writer = new FileWriter(csvFile);
    writer.write("title,type,state,rating\n");
    writer.write("Inception,MOVIE,FINISHED\n");
    writer.close();

    CsvImporterService importer = new CsvImporterService(csvFile.getAbsolutePath());
    List<CsvCinematic> result = importer.importData();

    assertNotNull(result);
    assertEquals(1, result.size());
    CsvCinematic cinematic = result.get(0);
    assertEquals("Inception", cinematic.getTitle());
    assertEquals(0, cinematic.getRating());
    assertEquals(State.FINISHED, cinematic.getState());
    assertEquals(Type.MOVIE, cinematic.getType());
  }


  @Test(expected = IOException.class)
  public void testImportData_FileNotFound() throws IOException {
    CsvImporterService importer = new CsvImporterService("non_existent_file.csv");
    importer.importData();
  }
}
