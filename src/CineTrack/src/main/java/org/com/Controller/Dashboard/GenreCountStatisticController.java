package org.com.Controller.Dashboard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.com.Models.Cinematic;
import org.com.Models.Enums.Type;
import org.com.Models.Statistic.GenreCountStrategy;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GenreCountStatisticController {
    @FXML
    private BarChart<String, Number> genreBarChart;
    @FXML
    private CategoryAxis genreAxis;
    @FXML
    private NumberAxis countAxis;
    @FXML
    public TextField minGenreCountInput;
    private Map<String, Integer> genreCountData;
    private VBox chartContainer;

    public GenreCountStatisticController() {
    }

    public GenreCountStatisticController(VBox chartContainer) {
        this.chartContainer = chartContainer;
    }

    public void setChartContainer(VBox chartContainer) {
        this.chartContainer = chartContainer;
    }

    public void addGenreCountStatistic(List<Cinematic> cinematics, List<Type> types) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GenreCountStatisticView.fxml"));
        HBox genreCountBlock = loader.load();

        GenreCountStatisticController controller = loader.getController();
        controller.setChartContainer(this.chartContainer);

        GenreCountStrategy genreCountStrategy = new GenreCountStrategy();
        Map<String, Integer> genreCountData = genreCountStrategy.calculate(cinematics, types);

        controller.setData(genreCountData, getMinGenreCountInput());

        if (chartContainer != null) {
            chartContainer.getChildren().add(genreCountBlock);
        }
    }

    public void setData(Map<String, Integer> genreCountData, int minCount) {
        this.genreCountData = genreCountData;
        updateChartData(minCount);
    }

    @FXML
    private void initialize() {
        initializeMinGenreCountListener();
    }

    private void initializeMinGenreCountListener() {
        minGenreCountInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                updateChart();
            }
        });
    }

    private void updateChart() {
        if (genreCountData != null) {
            setData(genreCountData, getMinGenreCountInput());
        }
    }

    private void updateChartData(int minCount) {
        if (genreBarChart == null) {
            return;
        }

        genreBarChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : genreCountData.entrySet()) {
            if (entry.getValue() >= minCount) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
        }
        genreBarChart.getData().add(series);
    }

    private int getMinGenreCountInput() {
        if (minGenreCountInput == null) {
            return 0;
        }
        try {
            return Integer.parseInt(minGenreCountInput.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}