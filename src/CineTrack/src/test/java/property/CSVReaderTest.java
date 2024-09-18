package property;

import Models.CSVLines;
import net.jqwik.api.*;
import net.jqwik.api.constraints.Size;
import Controller.CSVReader;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class CSVReaderTest {
    @Property
    void testReadCSV(
            @ForAll @Size(min = 100) List<@From("validCSVLine") String> csvLines) throws IOException {

        String tempFilePath = createTempCSVFile(csvLines);
        CSVReader csvReader = new CSVReader();

        List<CSVLines> result = csvReader.readCSV(tempFilePath);

        assertEquals(csvLines.size() - 1, result.size(), "Number of records should match number of input lines");
        assertTrue(result.stream().allMatch(record -> record.getSize() >= 3 && record.getSize() <= 4),
                "All records should have between 3 and 4 columns");

        Files.delete(Paths.get(tempFilePath));
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
