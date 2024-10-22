package org.com.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.com.models.enums.State;
import org.com.models.enums.Type;
import org.com.models.helper.ApiCinematic;
import org.com.models.helper.CsvCinematic;

public class Cinematic {

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
  private List<String> actors;
  private List<String> genres;

  public Cinematic(ApiCinematic apiCinematic, CsvCinematic csvCinematic) {
    this.title = csvCinematic.getTitle();
    this.myRating = csvCinematic.getRating();
    this.state = csvCinematic.getState();
    this.type = csvCinematic.getType();
    this.runtime = Optional.ofNullable(apiCinematic.getRuntime()).orElse("Unknown runtime");
    this.imdbRating = parseStringToDouble(apiCinematic.getImdbRating());
    this.imdbVotes = parseImdbVotes(apiCinematic.getImdbVotes());
    this.directorName = Optional.ofNullable(apiCinematic.getDirector()).orElse("Unknown director");
    this.description = Optional.ofNullable(apiCinematic.getPlot()).orElse("Unknown description");
    this.imageUrl = Optional.ofNullable(apiCinematic.getPosterUrl()).orElse("Unknown imageUrl");

    String genres = apiCinematic.getGenre();
    this.genres = (genres != null && !genres.isEmpty())
        ? Arrays.asList(genres.split(", "))
        : new ArrayList<>();

    String actors = apiCinematic.getActors();
    this.actors = (actors != null && !actors.isEmpty())
        ? Arrays.asList(actors.split(", "))
        : new ArrayList<>();
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getMyRating() {
    return myRating;
  }

  public void setMyRating(int myRating) {
    this.myRating = myRating;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getRuntime() {
    return runtime;
  }

  public void setRuntime(String runtime) {
    this.runtime = runtime;
  }

  public double getImdbRating() {
    return imdbRating;
  }

  public void setImdbRating(double imdbRating) {
    this.imdbRating = imdbRating;
  }

  public String getDirectorName() {
    return directorName;
  }

  public void setDirectorName(String directorName) {
    this.directorName = directorName;
  }

  public List<String> getGenres() {
    return genres;
  }

  public void setGenres(List<String> genres) {
    this.genres = genres;
  }

  public List<String> getActors() {
    return actors;
  }

  public void setActors(List<String> actors) {
    this.actors = actors;
  }

  private double parseStringToDouble(String str) {
    try {
      return Double.parseDouble(str);
    } catch (NumberFormatException e) {
      return 0.0;
    }
  }

  public int getImdbVotes() {
    return imdbVotes;
  }

  public void setImdbVotes(int imdbVotes) {
    this.imdbVotes = imdbVotes;
  }

  private int parseImdbVotes(String imdbVotesStr) {
    if (imdbVotesStr == null || imdbVotesStr.isEmpty()) {
      return 0;
    }

    String cleanedVotes = imdbVotesStr.replace(",", "");
    return Integer.parseInt(cleanedVotes);
  }
}

