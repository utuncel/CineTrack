package org.com.model.models;

public class DashboardModelSingleton {

  private static DashboardModel instance;

  private DashboardModelSingleton() {
  }

  public static DashboardModel getInstance() {
    if (instance == null) {
      instance = new DashboardModel();
    }
    return instance;
  }
}
