package org.com.model.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.com.model.enums.State;
import org.com.model.enums.Type;

/**
 * Represents a detailed cinematic entity, including user data and additional information from an
 * API.
 *
 * <p>The `Cinematic` class combines user-provided data (from a CSV file) with supplementary data
 * fetched from an external API. It provides various utility methods to handle and parse the
 * integrated data effectively.</p>
 *
 * <h3>Features:</h3>
 * <ul>
 *   <li>Integration of user-specific data with API information.</li>
 *   <li>Automatic handling of missing or malformed data using defaults and safe parsing.</li>
 *   <li>Support for lists of genres and actors parsed from delimited strings.</li>
 * </ul>
 *
 * @author umut
 * @version 1.0
 */
public class Cinematic {

  private Long id;
  private String title;
  private int myRating;
  private String description;
  private String imageUrl;
  private String runtime;
  private double imdbRating;
  private int imdbVotes;
  private String directorName;
  private State state;
  private Type type;
  private List<String> actors = new ArrayList<>();
  private List<String> genres = new ArrayList<>();
  private User user;

  /**
   * Protected no-argument constructor.
   * <p>This constructor is required Hibernate.</p>
   */
  protected Cinematic() {
  }

  /**
   * Constructs a `Cinematic` instance by combining data from an API and a CSV file.
   *
   * @param apiCinematic The API-provided cinematic data.
   * @param csvCinematic The CSV-provided cinematic data.
   * @param user         The user associated with this cinematic.
   */
  public Cinematic(ApiCinematic apiCinematic, CsvCinematic csvCinematic, User user) {
    this.user = user;
    initializeFromCsv(csvCinematic);
    initializeFromApi(apiCinematic);
  }

  /**
   * Initializes the cinematic with user-provided data from a CSV file.
   *
   * @param csvCinematic The CSV-provided cinematic data.
   */
  private void initializeFromCsv(CsvCinematic csvCinematic) {
    this.title = csvCinematic.getTitle();
    this.myRating = csvCinematic.getRating();
    this.state = csvCinematic.getState();
    this.type = csvCinematic.getType();
  }

  /**
   * Initializes the cinematic with additional data from an API.
   *
   * @param apiCinematic The API-provided cinematic data.
   */
  private void initializeFromApi(ApiCinematic apiCinematic) {
    this.runtime = Optional.ofNullable(apiCinematic.getRuntime()).orElse("Unknown runtime");
    this.imdbRating = parseStringToDouble(apiCinematic.getImdbRating());
    this.imdbVotes = parseImdbVotes(apiCinematic.getImdbVotes());
    this.directorName = Optional.ofNullable(apiCinematic.getDirector()).orElse("Unknown director");
    this.description = Optional.ofNullable(apiCinematic.getPlot()).orElse("Unknown description");
    this.imageUrl = Optional.ofNullable(apiCinematic.getPosterUrl()).orElse("Unknown imageUrl");
    this.genres = parseDelimitedString(apiCinematic.getGenre());
    this.actors = parseDelimitedString(apiCinematic.getActors());
  }

  /**
   * Parses a delimited string (e.g., "Actor1, Actor2") into a list of strings.
   *
   * @param input The input string to parse.
   * @return A list of strings extracted from the input, or an empty list if the input is
   * null/empty.
   */
  private List<String> parseDelimitedString(String input) {
    if (input == null || input.isEmpty()) {
      return new ArrayList<>();
    }
    return Arrays.asList(input.split(", "));
  }

  /**
   * Parses a string representation of a double value.
   *
   * @param str The string to parse.
   * @return The parsed double value, or 0.0 if parsing fails.
   */
  private double parseStringToDouble(String str) {
    try {
      return Double.parseDouble(str);
    } catch (NumberFormatException e) {
      return 0.0;
    }
  }

  /**
   * Parses a string representation of IMDb votes (e.g., "1,234") into an integer.
   *
   * @param imdbVotesStr The string to parse.
   * @return The parsed integer value, or 0 if parsing fails.
   */
  private int parseImdbVotes(String imdbVotesStr) {
    if (imdbVotesStr == null || imdbVotesStr.isEmpty()) {
      return 0;
    }
    try {
      return Integer.parseInt(imdbVotesStr.replace(",", ""));
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  /**
   * Gets the unique identifier for the cinematic.
   *
   * @return The ID of the cinematic.
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the unique identifier for the cinematic.
   *
   * @param id The ID to set.
   */
  public void setId(Long id) {
    this.id = id;
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
   * Gets the user's personal rating for the cinematic.
   *
   * @return The personal rating.
   */
  public int getMyRating() {
    return myRating;
  }

  /**
   * Sets the user's personal rating for the cinematic.
   *
   * @param myRating The personal rating to set.
   */
  public void setMyRating(int myRating) {
    this.myRating = myRating;
  }

  /**
   * Gets the current state of the cinematic.
   *
   * @return The state of the cinematic.
   */
  public State getState() {
    return state;
  }

  /**
   * Sets the current state of the cinematic.
   *
   * @param state The state to set (e.g., WATCHING, FINISHED).
   */
  public void setState(State state) {
    this.state = state;
  }

  /**
   * Gets the type of the cinematic (e.g., Movie, Series, Anime).
   *
   * @return The type of the cinematic.
   */
  public Type getType() {
    return type;
  }

  /**
   * Sets the type of the cinematic.
   *
   * @param type The type to set (e.g., MOVIE, SERIES).
   */
  public void setType(Type type) {
    this.type = type;
  }

  /**
   * Gets the URL of the cinematic's poster image.
   *
   * @return The poster image URL.
   */
  public String getImageUrl() {
    return imageUrl;
  }

  /**
   * Sets the URL of the cinematic's poster image.
   *
   * @param imageUrl The URL to set.
   */
  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  /**
   * Gets the description of the cinematic.
   *
   * @return The description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of the cinematic.
   *
   * @param description The description to set.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Gets the runtime of the cinematic in a human-readable format.
   *
   * @return The runtime of the cinematic.
   */
  public String getRuntime() {
    return runtime;
  }

  /**
   * Sets the runtime of the cinematic.
   *
   * @param runtime The runtime to set.
   */
  public void setRuntime(String runtime) {
    this.runtime = runtime;
  }

  /**
   * Gets the IMDb rating of the cinematic.
   *
   * @return The IMDb rating.
   */
  public double getImdbRating() {
    return imdbRating;
  }

  /**
   * Sets the IMDb rating of the cinematic.
   *
   * @param imdbRating The IMDb rating to set.
   */
  public void setImdbRating(double imdbRating) {
    this.imdbRating = imdbRating;
  }

  /**
   * Gets the IMDb vote count for the cinematic.
   *
   * @return The IMDb vote count.
   */
  public int getImdbVotes() {
    return imdbVotes;
  }

  /**
   * Sets the IMDb vote count for the cinematic.
   *
   * @param imdbVotes The IMDb vote count to set.
   */
  public void setImdbVotes(int imdbVotes) {
    this.imdbVotes = imdbVotes;
  }

  /**
   * Gets the director's name for the cinematic.
   *
   * @return The director's name.
   */
  public String getDirectorName() {
    return directorName;
  }

  /**
   * Sets the director's name for the cinematic.
   *
   * @param directorName The director's name to set.
   */
  public void setDirectorName(String directorName) {
    this.directorName = directorName;
  }

  /**
   * Gets the list of genres associated with the cinematic.
   *
   * @return The list of genres.
   */
  public List<String> getGenres() {
    return genres;
  }

  /**
   * Sets the list of genres associated with the cinematic.
   *
   * @param genres The list of genres to set.
   */
  public void setGenres(List<String> genres) {
    this.genres = genres;
  }

  /**
   * Gets the list of actors associated with the cinematic.
   *
   * @return The list of actors.
   */
  public List<String> getActors() {
    return actors;
  }

  /**
   * Sets the list of actors associated with the cinematic.
   *
   * @param actors The list of actors to set.
   */
  public void setActors(List<String> actors) {
    this.actors = actors;
  }

  /**
   * Gets the user associated with this cinematic.
   *
   * @return The associated user.
   */
  public User getUser() {
    return user;
  }

  /**
   * Sets the user associated with this cinematic.
   *
   * @param user The user to associate with this cinematic.
   */
  public void setUser(User user) {
    this.user = user;
  }

}