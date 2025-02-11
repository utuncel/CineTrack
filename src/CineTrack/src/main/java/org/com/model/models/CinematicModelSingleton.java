package org.com.model.models;

public class CinematicModelSingleton {

  private static CinematicModel instance;

  private CinematicModelSingleton() {
  }

  public static CinematicModel getInstance() {
    if (instance == null) {
      instance = new CinematicModel();
    }
    return instance;
  }
}
