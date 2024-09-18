package junit;

import Controller.CSVReader;
import Models.CSVLines;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CSVReaderTest {

    private CSVReader csvReader;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        csvReader = new CSVReader();
        tempFile = File.createTempFile("Test", ".csv");
    }

    @Test
    void testReadCSVValid() throws IOException {
        String validCSVContent = "column1,column2,column3,column4\n" +
                "value1,value2,value3,value4\n" +
                "data1,data2,data3\n" +
                "data1,data2,,data4\n";

        writeToFile(validCSVContent);

        List<CSVLines> records = csvReader.readCSV(tempFile.getAbsolutePath());

        assertEquals(2, records.size());
        assertEquals(List.of("value1", "value2", "value3", "value4"), records.get(0).getColumns());
        assertEquals(List.of("data1", "data2", "data3"), records.get(1).getColumns());
    }

    @Test
    void testReadCSVInvalid() throws IOException {
        String invalidCSVContent = "tooFew\n" +
                "justTwo,columns\n" +
                "valid1,valid2,valid3\n";

        writeToFile(invalidCSVContent);

        assertThrows(RuntimeException.class, () -> {
            List<CSVLines> lines = csvReader.readCSV(tempFile.getAbsolutePath());
        });
    }

    @Test
    void testReadCSV_ExceptionOnFileNotFound() {
        assertThrows(RuntimeException.class, () -> csvReader.readCSV("non_existent_file.csv"));
    }

    // help-method that I can write some csv content into a file
    private void writeToFile(String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(content);
        }
    }
}