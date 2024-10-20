package org.com.Controller.Dashboard;

import org.com.Models.DashboardModel;

public class DashboardModelSingleton {
    private static DashboardModel instance;

    private DashboardModelSingleton() {}

    public static DashboardModel getInstance() {
        if (instance == null) {
            instance = new DashboardModel();
        }
        return instance;
    }
}
