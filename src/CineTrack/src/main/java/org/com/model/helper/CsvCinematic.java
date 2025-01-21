package org.com.model.helper;

import org.com.model.enums.State;
import org.com.model.enums.Type;

public class CsvCinematic {

  private final String title;
  private final int rating;
  private final State state;
  private final Type type;

  private CsvCinematic(Builder builder) {
    this.title = builder.title;
    this.state = builder.state;
    this.type = builder.type;
    this.rating = builder.rating;
  }

  public String getTitle() {
    return title;
  }

  public int getRating() {
    return rating;
  }

  public State getState() {
    return state;
  }

  public Type getType() {
    return type;
  }

  public static class Builder {

    private String title;
    private int rating;
    private State state;
    private Type type;

    public Builder withTitle(String title) {
      this.title = title;
      return this;
    }

    public Builder withRating(int rating) {
      this.rating = rating;
      return this;
    }

    public Builder withState(State state) {
      this.state = state;
      return this;
    }

    public Builder withType(Type type) {
      this.type = type;
      return this;
    }

    public CsvCinematic build() {
      return new CsvCinematic(this);
    }
  }


}
