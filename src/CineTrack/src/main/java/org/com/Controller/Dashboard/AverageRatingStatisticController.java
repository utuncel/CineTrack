package org.com.Controller.Dashboard;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.com.Models.Cinematic;
import org.com.Models.Enums.State;
import org.com.Models.Enums.Type;
import org.com.Models.Statistic.ImdbRatingAverageStrategy;
import org.com.Models.Statistic.MyRatingAverageStrategy;

import java.io.IOException;
import java.util.List;

public class AverageRatingStatisticController {
    @FXML
    public Label myRatingValue;
    @FXML
    public Label imdbRatingValue;

    private VBox chartContainer;

    public AverageRatingStatisticController() {
    }

    public AverageRatingStatisticController(VBox chartContainer) {
        this.chartContainer = chartContainer;
    }

    public void setChartContainer(VBox chartContainer) {
        this.chartContainer = chartContainer;
    }

    public void setData(String myRatingValue, String imdbRatingValue) {
        this.myRatingValue.setText(myRatingValue);
        this.imdbRatingValue.setText(imdbRatingValue);
    }

    public void addAverageRatingStatistic(List<Cinematic> cinematics, List<Type> types, List<State> states) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AverageRatingStatisticView.fxml"));
        HBox personalRatingBlock = loader.load();

        AverageRatingStatisticController controller = loader.getController();
        controller.setChartContainer(this.chartContainer);

        MyRatingAverageStrategy myAverageRatingStrategy = new MyRatingAverageStrategy();
        ImdbRatingAverageStrategy imdbAverageRatingStrategy = new ImdbRatingAverageStrategy();

        controller.setData(
                String.valueOf(myAverageRatingStrategy.calculate(cinematics, types, states)),
                String.valueOf(imdbAverageRatingStrategy.calculate(cinematics, types, states))
        );

        if (chartContainer != null) {
            chartContainer.getChildren().add(personalRatingBlock);
        }
    }
}
