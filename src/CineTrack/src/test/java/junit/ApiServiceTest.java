package junit;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.com.model.domain.ApiCinematic;
import org.com.service.ApiService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ApiServiceTest {

  @InjectMocks
  private ApiService apiService;

  @Before
  public void setUp() {
    System.setProperty("OMDb_API", "test_api_key");
  }

  @Test
  public void testFetchMoviesOrSeries_SuccessfulResponse() {

    ApiCinematic result = apiService.fetchMoviesOrSeries("Inception");

    assertNotNull(result);
    assertEquals("Inception", result.getTitle());
  }

  @Test
  public void testFetchMoviesOrSeries_NotFound() {
    ApiCinematic result = apiService.fetchMoviesOrSeries("Unknown Movie__1");

    assertNull(result);
  }
}
