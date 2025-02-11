package junit;

import static org.junit.Assert.assertEquals;

import org.com.model.enums.State;
import org.com.model.enums.Type;
import org.com.service.CsvParserService;
import org.junit.Test;

public class CsvParserServiceTest extends JavaFXTestBase {

  @Test
  public void testParseTypes_ValidType() {
    CsvParserService parserService = new CsvParserService(1);
    Type result = parserService.parseTypes("MOVIE");
    assertEquals(Type.MOVIE, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseTypes_InvalidType() {
    CsvParserService parserService = new CsvParserService(1);
    parserService.parseTypes("INVALID_TYPE");
  }

  @Test
  public void testParseStates_ValidState() {
    CsvParserService parserService = new CsvParserService(1);
    State result = parserService.parseStates("WATCHING");
    assertEquals(State.WATCHING, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseStates_InvalidState() {
    CsvParserService parserService = new CsvParserService(1);
    parserService.parseStates("INVALID_STATE");
  }

  @Test
  public void testParseStringToInt_ValidRating() {
    CsvParserService parserService = new CsvParserService(1);
    int result = parserService.parseStringToInt("5");
    assertEquals(5, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseStringToInt_InvalidRatingLow() {
    CsvParserService parserService = new CsvParserService(1);
    parserService.parseStringToInt("0");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseStringToInt_InvalidRatingHigh() {
    CsvParserService parserService = new CsvParserService(1);
    parserService.parseStringToInt("11");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseStringToInt_NonNumericRating() {
    CsvParserService parserService = new CsvParserService(1);
    parserService.parseStringToInt("abc");
  }

  @Test
  public void testDefaultConstructor() {
    CsvParserService parserService = new CsvParserService();
    // Verify no exceptions are thrown
  }
}