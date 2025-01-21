package org.com.controller.dashboard;

import org.com.model.models.DashboardModel;

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
