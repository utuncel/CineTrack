package org.com.Controller.Dashboard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.com.Models.Cinematic;
import org.com.Models.Enums.State;
import org.com.Models.Enums.Type;
import org.com.Models.Records.StateCount;
import org.com.Models.Statistic.StateCountStrategy;

import java.io.IOException;
import java.util.List;

public class StateCountStatisticController {
    @FXML
    private Label finished;
    @FXML
    private Label watching;
    @FXML
    private Label dropped;
    @FXML
    private Label toWatch;

    private VBox chartContainer;

    public StateCountStatisticController() {
    }

    public StateCountStatisticController(VBox chartContainer) {
        this.chartContainer = chartContainer;
    }

    public void setChartContainer(VBox chartContainer) {
        this.chartContainer = chartContainer;
    }

    public void addStateCountStatistic(List<Cinematic> cinematics, List<Type> types, List<State> states) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/StateCountStatisticView.fxml"));
        HBox fourBlockStatistic = loader.load();

        StateCountStatisticController controller = loader.getController();
        controller.setChartContainer(this.chartContainer);

        StateCountStrategy stateCountStrategy = new StateCountStrategy();
        StateCount genreCountData = stateCountStrategy.calculate(cinematics, types,states);

        controller.setBlockValues(String.valueOf(genreCountData.finished()), String.valueOf(genreCountData.watching()), String.valueOf(genreCountData.dropped()), String.valueOf(genreCountData.towatch()));

        if (chartContainer != null) {
            chartContainer.getChildren().add(fourBlockStatistic);
        }
    }

    public void setBlockValues(String finishedCount, String watchedCount, String droppedCount, String toWatchCount) {
        finished.setText(finishedCount);
        watching.setText(watchedCount);
        dropped.setText(droppedCount);
        toWatch.setText(toWatchCount);
    }
}
