package org.com.models.helper;

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

  public void setImdbVotes(String imdbVotes) {
    this.imdbVotes = imdbVotes;
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

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public String getRuntime() {
    return runtime;
  }

  public void setRuntime(String runtime) {
    this.runtime = runtime;
  }

  public String getDirector() {
    return director;
  }

  public void setDirector(String director) {
    this.director = director;
  }

  public String getActors() {
    return actors;
  }

  public void setActors(String actors) {
    this.actors = actors;
  }

  public String getPlot() {
    return plot;
  }

  public void setPlot(String plot) {
    this.plot = plot;
  }

  public String getPosterUrl() {
    return posterUrl;
  }

  public void setPosterUrl(String posterUrl) {
    this.posterUrl = posterUrl;
  }

  public String getImdbRating() {
    return imdbRating;
  }

  public void setImdbRating(String imdbRating) {
    this.imdbRating = imdbRating;
  }

}
