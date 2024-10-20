package org.com.Service;

import org.com.Models.Helper.ApiCinematic;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class ApiData {

    private final String MovieAndSeriesAPI = System.getenv("OMDb_API");

    public ApiCinematic fetchMoviesOrSeries(String title) {
        try {
            HttpResponse<String> response;
            try (HttpClient client = HttpClient.newHttpClient()) {

                String req = String.format("https://www.omdbapi.com/?apikey=%s&t=%s", MovieAndSeriesAPI, formatTitleForApiRequest(title));

                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(req)).build();

                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            }

            String responseBody = response.body();
            System.out.println(responseBody);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            if (rootNode.has("Response") && rootNode.get("Response").asText().equals("False")) {
                return null;
            }

            return objectMapper.readValue(responseBody, ApiCinematic.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String formatTitleForApiRequest(String title) {
        return title.replaceAll(" ", "%20");
    }

}
