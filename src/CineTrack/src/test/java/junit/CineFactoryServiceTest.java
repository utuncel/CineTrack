package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javafx.application.Platform;
import org.com.model.domain.ApiCinematic;
import org.com.model.domain.Cinematic;
import org.com.model.domain.CsvCinematic;
import org.com.model.enums.State;
import org.com.model.enums.Type;
import org.com.service.ApiService;
import org.com.service.CineFactoryService;
import org.com.service.CsvImporterService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CineFactoryServiceTest extends JavaFXTestBase {

  private ApiService apiService;
  private CsvImporterService csvImporterService;
  private CineFactoryService cineFactoryService;

  @Before
  public void setUp() {
    apiService = mock(ApiService.class);
    csvImporterService = mock(CsvImporterService.class);
    cineFactoryService = new CineFactoryService(csvImporterService, apiService, null);
  }

  @Test
  public void testCreateCinematic_Success() {
    CsvCinematic csvCinematic = mock(CsvCinematic.class);
    when(csvCinematic.getTitle()).thenReturn("Inception");
    when(csvCinematic.getRating()).thenReturn(5);
    when(csvCinematic.getState()).thenReturn(State.WATCHING);
    when(csvCinematic.getType()).thenReturn(Type.MOVIE);

    ApiCinematic apiCinematic = mock(ApiCinematic.class);
    when(apiCinematic.getRuntime()).thenReturn("148 min");
    when(apiCinematic.getImdbRating()).thenReturn(
        "8.8");
    when(apiCinematic.getImdbVotes()).thenReturn("2,000");
    when(apiCinematic.getDirector()).thenReturn("Christopher Nolan");
    when(apiCinematic.getPlot()).thenReturn("A thief who steals corporate secrets...");
    when(apiCinematic.getPosterUrl()).thenReturn("https://example.com/inception.jpg");
    when(apiCinematic.getGenre()).thenReturn("Action, Sci-Fi");
    when(apiCinematic.getActors()).thenReturn("Leonardo DiCaprio, Joseph Gordon-Levitt");

    when(apiService.fetchMoviesOrSeries("Inception")).thenReturn(apiCinematic);

    Cinematic result = cineFactoryService.createCinematic(csvCinematic);

    assertNotNull(result);
  }


  @Test
  public void testCreateCinematic_NotFound() {
    CsvCinematic csvCinematic = mock(CsvCinematic.class);
    when(csvCinematic.getTitle()).thenReturn("Unknown Movie");
    when(apiService.fetchMoviesOrSeries("Unknown Movie")).thenReturn(null);

    Cinematic result = cineFactoryService.createCinematic(csvCinematic);

    assertNull(result);
    verify(apiService).fetchMoviesOrSeries("Unknown Movie");
  }

  @Test
  public void testCreateCinematics_Success() throws IOException {
    CsvCinematic csvCinematic = mock(CsvCinematic.class);
    when(csvCinematic.getTitle()).thenReturn("Inception");
    when(csvCinematic.getRating()).thenReturn(5);
    when(csvCinematic.getState()).thenReturn(State.WATCHING);
    when(csvCinematic.getType()).thenReturn(Type.MOVIE);

    ApiCinematic apiCinematic = mock(ApiCinematic.class);
    when(apiCinematic.getRuntime()).thenReturn("148 min");
    when(apiCinematic.getImdbRating()).thenReturn("8.8");
    when(apiCinematic.getImdbVotes()).thenReturn("2,000");
    when(apiCinematic.getDirector()).thenReturn("Christopher Nolan");
    when(apiCinematic.getPlot()).thenReturn("A thief who steals corporate secrets...");
    when(apiCinematic.getPosterUrl()).thenReturn("https://example.com/inception.jpg");
    when(apiCinematic.getGenre()).thenReturn("Action, Sci-Fi");
    when(apiCinematic.getActors()).thenReturn("Leonardo DiCaprio, Joseph Gordon-Levitt");

    when(csvImporterService.importData()).thenReturn(Collections.singletonList(csvCinematic));
    when(apiService.fetchMoviesOrSeries("Inception")).thenReturn(apiCinematic);

    List<Cinematic> result = cineFactoryService.createCinematics();

    assertEquals(1, result.size());
  }


  @Test(expected = IOException.class)
  public void testCreateCinematics_CsvImportFails() throws IOException {
    when(csvImporterService.importData()).thenThrow(new IOException());
    cineFactoryService.createCinematics();
  }

  @Test
  public void testCreateCinematics_EmptyCsv() throws IOException {
    when(csvImporterService.importData()).thenReturn(Collections.emptyList());

    List<Cinematic> result = cineFactoryService.createCinematics();

    assertTrue(result.isEmpty());
    verify(csvImporterService).importData();
  }
}