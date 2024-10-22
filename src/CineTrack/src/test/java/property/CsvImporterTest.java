package property;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.From;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.constraints.Size;
import org.com.models.helper.CsvCinematic;
import org.com.service.CsvImporter;

class CsvImporterTest {

  @Property
  void testImportData(
      @ForAll @Size(min = 100) List<@From("validCSVLine") String> csvLines) throws IOException {

    String tempFilePath = createTempCSVFile(csvLines);
    Path path = Paths.get(tempFilePath);
    CsvImporter csvImporter = new CsvImporter(path.toString());

    List<CsvCinematic> cinematics = csvImporter.importData();

    assertEquals(csvLines.size() - 1, cinematics.size(),
        "Number of records should match number of input lines");

    Files.delete(path);
  }

  @Provide
  Arbitrary<String> validCSVLine() { // ["�,�, ,�"]
    return Arbitraries.strings()
        .alpha().ofMinLength(1).ofMaxLength(50)
        .list().ofSize(4)
        .map(parts -> String.join(",", parts));
  }

  private String createTempCSVFile(List<String> lines) throws IOException {
    Path tempFile = Files.createTempFile("test", ".csv");
    Files.write(tempFile, lines);
    return tempFile.toString();
  }

}
