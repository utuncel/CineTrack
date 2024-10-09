package org.com.Models;

import org.com.Models.Enums.StatisticStrategy;
import org.com.Models.Enums.Type;

import java.util.ArrayList;
import java.util.List;

public class DashboardModel {
    public interface Listener {
        void onChange(DashboardModel dashboardModel);
    }

    private List<Cinematic> cinematics = new ArrayList<>();
    private List<Type> types = new ArrayList<>();
    private List<StatisticStrategy> statisticStrategies = new ArrayList<>();
    private List<Listener> listeners = new ArrayList<Listener>();

    public DashboardModel() {
        types.add(Type.MOVIE);
        types.add(Type.ANIME);
        types.add(Type.SERIES);
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public List<Cinematic> getCinematics() {
        return cinematics;
    }

    public void setCinematics(List<Cinematic> cinematics) {
        this.cinematics = cinematics;
        notifyObservers();
    }

    private void notifyObservers() {
        listeners.forEach(listener -> listener.onChange(this));
    }

    public void addType(Type type) {
        if (!types.contains(type)) {
            types.add(type);
        }
    }

    public void removeType(Type type) {
        types.remove(type);
    }

    public List<Type> getTypes() {
        return types;
    }

    public void addStatisticStrategy(StatisticStrategy statisticStrategy) {
        if (!statisticStrategies.contains(statisticStrategy)) {
            statisticStrategies.add(statisticStrategy);
        }
    }

    public void removeStatisticStrategy(StatisticStrategy statisticStrategy) {
        statisticStrategies.remove(statisticStrategy);
    }

    public List<StatisticStrategy> getStatisticStrategy() {
        return statisticStrategies;
    }
}
