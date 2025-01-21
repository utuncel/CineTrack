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
import org.com.model.helper.ApiCinematic;

public class ApiService {

  private static final String API_BASE_URL = "https://www.omdbapi.com/";
  private static final HttpClient client = HttpClient.newHttpClient();
  private final String apiKey;
  private final LogService logger = LogService.getInstance();

  public ApiService() {
    this.apiKey = System.getenv("OMDb_API");
    if (apiKey == null || apiKey.isEmpty()) {
      logger.logError("API key for OMDb is not set in the environment variables.");
      throw new IllegalStateException("API key for OMDb is required but not set.");
    }
  }

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

  private String buildRequestUrl(String title) {
    String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
    return String.format("%s?apikey=%s&t=%s", API_BASE_URL, apiKey, encodedTitle);
  }

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

  private ApiCinematic parseApiResponse(String responseBody) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode rootNode = objectMapper.readTree(responseBody);

    if (rootNode.has("Response") && rootNode.get("Response").asText().equalsIgnoreCase("False")) {
      return null;
    }
    return objectMapper.readValue(responseBody, ApiCinematic.class);
  }
}
