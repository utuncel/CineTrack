package org.com.Controller.Dashboard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.com.Models.Cinematic;
import org.com.Models.Enums.State;
import org.com.Models.Enums.Type;
import org.com.Models.Records.TypeCount;
import org.com.Models.Statistic.TypeCountStrategy;

import java.io.IOException;
import java.util.List;

public class TypeCountStatisticController {
    @FXML
    public Label movie;
    @FXML
    public Label series;
    @FXML
    public Label anime;

    private VBox chartContainer;

    public TypeCountStatisticController() {
    }

    public TypeCountStatisticController(VBox chartContainer) {
        this.chartContainer = chartContainer;
    }

    public void setChartContainer(VBox chartContainer) {
        this.chartContainer = chartContainer;
    }

    public void addTypeCountStatistic(List<Cinematic> cinematics, List<Type> types, List<State> states) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TypeCountStatisticView.fxml"));
        HBox fourBlockStatistic = loader.load();

        TypeCountStatisticController controller = loader.getController();
        controller.setChartContainer(this.chartContainer);

        TypeCountStrategy typeCountStrategy = new TypeCountStrategy();
        TypeCount typeCountData = typeCountStrategy.calculate(cinematics, types, states);

        controller.setBlockValues(String.valueOf(typeCountData.movie()), String.valueOf(typeCountData.series()), String.valueOf(typeCountData.anime()));

        if (chartContainer != null) {
            chartContainer.getChildren().add(fourBlockStatistic);
        }
    }

    public void setBlockValues(String movieCount, String seriesCount, String animeCount) {
        movie.setText(movieCount);
        series.setText(seriesCount);
        anime.setText(animeCount);
    }
}
