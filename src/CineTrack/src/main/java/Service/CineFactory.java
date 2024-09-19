package Service;

import Models.CSVLine;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.IOException;
import java.util.List;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CineFactory {

    private final String  MovieAndSeriesAPI = System.getenv("OMDb_API");
    private List<CSVLine> lines;


    public CineFactory(List<CSVLine> lines) {
        this.lines = lines;

    }

    public void fillMoviesAndSeries() {

        try {
            HttpClient client = HttpClient.newHttpClient();

            for(int i = 0; i < 3; i++) {
            String req = String.format("https://www.omdbapi.com/?apikey=%s&t=%s", MovieAndSeriesAPI, lines.get(i).getColumns().getFirst());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(req))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            System.out.println(responseBody);

            ObjectMapper objectMapper = new ObjectMapper();
            MoviesAndSeriesAPIResponse apiResponse = objectMapper.readValue(response.body(), MoviesAndSeriesAPIResponse.class);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class MoviesAndSeriesAPIResponse {
        @JsonProperty("Title")
        private String title;

        @JsonProperty("Year")
        private String year;

        @JsonProperty("Runtime")
        private String runtime;

        @JsonProperty("Director")
        private String director;

        @JsonProperty("Actors")
        private String actors;

        @JsonProperty("Plot")
        private String plot;

        @JsonProperty("Poster")
        private String posterUrl;

        @JsonProperty("imdbRating")
        private String imdbRating;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getYear() { return year; }
        public void setYear(String year) { this.year = year; }

        public String getRuntime() { return runtime; }
        public void setRuntime(String runtime) { this.runtime = runtime; }

        public String getDirector() { return director; }
        public void setDirector(String director) { this.director = director; }

        public String getActors() { return actors; }
        public void setActors(String actors) { this.actors = actors; }

        public String getPlot() { return plot; }
        public void setPlot(String plot) { this.plot = plot; }

        public String getPosterUrl() { return posterUrl; }
        public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

        public String getImdbRating() { return imdbRating; }
        public void setImdbRating(String imdbRating) { this.imdbRating = imdbRating; }
    }
}
