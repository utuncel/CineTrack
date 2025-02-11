package junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.com.service.LineValidatorService;
import org.junit.Test;

public class LineValidatorServiceTest extends JavaFXTestBase {

  private LineValidatorService validatorService = new LineValidatorService();

  @Test
  public void testValidHeaderLength_Correct() {
    assertTrue(validatorService.isValidHeaderLength("Title,Type,State,Rating"));
  }

  @Test
  public void testValidHeaderLength_Incorrect() {
    assertFalse(validatorService.isValidHeaderLength("Title,Type,State"));
    assertFalse(validatorService.isValidHeaderLength("Title,Type,State,Rating,Extra"));
  }

  @Test
  public void testValidHeader_Correct() {
    assertTrue(validatorService.isValidHeader("Title,Type,State,Rating"));
    assertTrue(validatorService.isValidHeader("title,type,state,rating"));
  }

  @Test
  public void testValidHeader_Incorrect() {
    assertFalse(validatorService.isValidHeader("Wrong,Headers,Here,Totally"));
  }

  @Test
  public void testValidRecord_Correct() {
    assertTrue(validatorService.isValidRecord(new String[]{"Inception", "MOVIE", "WATCHING"}));
    assertTrue(validatorService.isValidRecord(new String[]{"Inception", "MOVIE", "WATCHING", "5"}));
  }

  @Test
  public void testValidRecord_MissingRequiredFields() {
    assertFalse(validatorService.isValidRecord(new String[]{"", "MOVIE", "WATCHING"}));
    assertFalse(validatorService.isValidRecord(new String[]{"Inception", "", "WATCHING"}));
    assertFalse(validatorService.isValidRecord(new String[]{"Inception", "MOVIE", ""}));
  }

  @Test
  public void testValidRecord_BlankRating() {
    assertFalse(validatorService.isValidRecord(new String[]{"Inception", "MOVIE", "WATCHING", ""}));
  }

  @Test
  public void testValidRecord_InvalidColumnCount() {
    assertFalse(validatorService.isValidRecord(new String[]{"Inception"}));
    assertFalse(validatorService.isValidRecord(
        new String[]{"Inception", "MOVIE", "WATCHING", "5", "Extra"}));
  }
}