package org.com.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.com.model.domain.ApiCinematic;

/**
 * Service for fetching movie and series information via the OMDb API (In Our Context there is a
 * difference between anime and series but OMDb does not make this difference). Enables retrieving
 * media data based on a title.
 *
 * @author Umut
 * @version 1.0
 */

public class ApiService {

  private static final String API_BASE_URL = "https://www.omdbapi.com/";
  private static final HttpClient client = HttpClient.newHttpClient();
  private final String apiKey;
  private final LogService logger = LogService.getInstance();

  /**
   * Constructor that loads the API key from environment variables.
   *
   * @throws IllegalStateException if no API key is set
   */
  public ApiService() {
    this.apiKey = System.getenv("OMDb_API");
    if (apiKey == null || apiKey.isEmpty()) {
      logger.logError("API key for OMDb is not set in the environment variables.");
      throw new IllegalStateException("API key for OMDb is required but not set.");
    }
  }

  /**
   * Searches for movie or series information through OMDb API.
   *
   * @param title The title of the movie, series or anime
   * @return ApiCinematic object with media information or null if no result
   */
  public ApiCinematic fetchMoviesOrSeries(String title) {
    try {
      String url = buildRequestUrl(title);
      String responseBody = sendHttpRequest(url);
      return parseApiResponse(responseBody);
    } catch (IOException | InterruptedException e) {
      logger.logError(e.getMessage());
      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }
      return null;
    }
  }

  /**
   * Creates the API request URL with URL-encoded title.
   *
   * @param title The title to search for
   * @return Complete URL for the API request
   */
  private String buildRequestUrl(String title) {
    String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
    return String.format("%s?apikey=%s&t=%s", API_BASE_URL, apiKey, encodedTitle);
  }

  /**
   * Sends an HTTP request to the OMDb API.
   *
   * @param url Complete API request URL
   * @return Response body as String
   * @throws IOException          for connection issues
   * @throws InterruptedException if request is interrupted
   */
  private String sendHttpRequest(String url) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .build();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      return response.body();
    } catch (IOException | InterruptedException e) {
      logger.logError(e.getMessage());
      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }
    }
    return null;
  }

  /**
   * Processes the JSON response from OMDb API.
   *
   * @param responseBody JSON response from API
   * @return ApiCinematic object or null for invalid response
   * @throws IOException for parsing errors
   */
  private ApiCinematic parseApiResponse(String responseBody) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode rootNode = objectMapper.readTree(responseBody);

    if (rootNode.has("Response") && rootNode.get("Response").asText().equalsIgnoreCase("False")) {
      return null;
    }
    return objectMapper.readValue(responseBody, ApiCinematic.class);
  }
}
