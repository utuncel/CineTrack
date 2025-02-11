package org.com.model.models;

public class DashboardModelSingleton {

  private static CinematicModel instance;

  private DashboardModelSingleton() {
  }

  public static CinematicModel getInstance() {
    if (instance == null) {
      instance = new CinematicModel();
    }
    return instance;
  }
}
