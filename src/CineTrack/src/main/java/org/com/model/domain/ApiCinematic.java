package org.com.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents cinematic data retrieved from an external API. This class is mapped to JSON fields
 * using Jackson annotations.
 *
 * @author umut
 * @version 1.0
 */
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

  /**
   * Gets the IMDb votes count for the cinematic.
   *
   * @return The IMDb votes count as a string.
   */
  public String getImdbVotes() {
    return imdbVotes;
  }

  /**
   * Gets the genre(s) of the cinematic.
   *
   * @return The genre(s) as a comma-separated string.
   */
  public String getGenre() {
    return genre;
  }

  /**
   * Sets the genre(s) of the cinematic.
   *
   * @param genre The genre(s) to set as a comma-separated string.
   */
  public void setGenre(String genre) {
    this.genre = genre;
  }

  /**
   * Gets the title of the cinematic.
   *
   * @return The title of the cinematic.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of the cinematic.
   *
   * @param title The title to set.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Gets the runtime of the cinematic.
   *
   * @return The runtime of the cinematic.
   */
  public String getRuntime() {
    return runtime;
  }

  /**
   * Gets the director of the cinematic.
   *
   * @return The name of the director.
   */
  public String getDirector() {
    return director;
  }

  /**
   * Gets the actors in the cinematic.
   *
   * @return A comma-separated string of actor names.
   */
  public String getActors() {
    return actors;
  }

  /**
   * Gets the plot of the cinematic.
   *
   * @return The plot summary of the cinematic.
   */
  public String getPlot() {
    return plot;
  }

  /**
   * Gets the URL of the poster image for the cinematic.
   *
   * @return The poster image URL.
   */
  public String getPosterUrl() {
    return posterUrl;
  }

  /**
   * Gets the IMDb rating of the cinematic.
   *
   * @return The IMDb rating as a string.
   */
  public String getImdbRating() {
    return imdbRating;
  }
}
