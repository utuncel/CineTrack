package junit;

import Controller.CsvImporter;
import Models.CSVLines;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CsvImporterTest {

    private CsvImporter csvImporter;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        csvImporter = new CsvImporter();
        tempFile = File.createTempFile("Test", ".csv");
    }

    @Test
    void testImportDataValid() throws IOException {
        String validCSVContent = "column1,column2,column3,column4\n" +
                "value1,value2,value3,value4\n" +
                "data1,data2,data3\n" +
                "data1,data2,,data4\n";

        writeToFile(validCSVContent);

        List<CSVLines> records = csvImporter.importData(tempFile.getAbsolutePath());

        assertEquals(2, records.size());
        assertEquals(List.of("value1", "value2", "value3", "value4"), records.get(0).getColumns());
        assertEquals(List.of("data1", "data2", "data3"), records.get(1).getColumns());
    }

    @Test
    void testImportDataInvalid() throws IOException {
        String invalidCSVContent = "tooFew\n" +
                "justTwo,columns\n" +
                "valid1,valid2,valid3\n";

        writeToFile(invalidCSVContent);

        assertThrows(RuntimeException.class, () -> {
            List<CSVLines> lines = csvImporter.importData(tempFile.getAbsolutePath());
        });
    }

    @Test
    void testImportData_ExceptionOnFileNotFound() {
        assertThrows(RuntimeException.class, () -> csvImporter.importData("non_existent_file.csv"));
    }

    // help-method that I can write some csv content into a file
    private void writeToFile(String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(content);
        }
    }
}