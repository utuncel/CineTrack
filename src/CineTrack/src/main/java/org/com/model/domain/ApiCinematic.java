package org.com.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiCinematic {

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

  @JsonProperty("Genre")
  private String genre;

  @JsonProperty("imdbVotes")
  private String imdbVotes;

  public String getImdbVotes() {
    return imdbVotes;
  }

  public String getGenre() {
    return genre;
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getRuntime() {
    return runtime;
  }

  public String getDirector() {
    return director;
  }

  public String getActors() {
    return actors;
  }

  public String getPlot() {
    return plot;
  }

  public String getPosterUrl() {
    return posterUrl;
  }

  public String getImdbRating() {
    return imdbRating;
  }
}
