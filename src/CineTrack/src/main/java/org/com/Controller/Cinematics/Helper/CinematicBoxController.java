package org.com.Controller.Cinematics.Helper;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import org.com.Models.Cinematic;

import java.util.List;

public class CinematicBoxController {

    @FXML
    public TextArea descriptionArea;
    @FXML
    public FlowPane genresPane;
    @FXML
    public FlowPane actorsPane;
    @FXML
    public Label directorLabel;
    @FXML
    public Label runtimeLabel;
    @FXML
    public ImageView posterImage;
    @FXML
    public Label imdbRatingLabel;
    @FXML
    public Label myRatingLabel;
    @FXML
    public Label titleLabel;
    @FXML
    public Label stateLabel;
    @FXML
    public ImageView imageView;

    public void setCinematicView(Cinematic cinematic) {
        posterImage.setImage(new Image(cinematic.getImageUrl()));

        titleLabel.setText(cinematic.getTitle());
        runtimeLabel.setText("Laufzeit: " + cinematic.getRuntime());
        imdbRatingLabel.setText(String.format("IMDb: %.1f/10 (%,d Stimmen)",
                cinematic.getImdbRating(), cinematic.getImdbVotes()));
        myRatingLabel.setText("Meine Bewertung: " + cinematic.getMyRating() + "/10");
        directorLabel.setText("Regie: " + cinematic.getDirectorName());
        stateLabel.setText("Status: " + cinematic.getState().toString());
        descriptionArea.setText(cinematic.getDescription());

        setChips(cinematic.getGenres(), genresPane);

        setChips(cinematic.getActors(), actorsPane);
    }

    private void setChips(List<String> items, FlowPane pane) {
        pane.getChildren().clear();
        for (String item : items) {
            Label chip = new Label(item);
            chip.getStyleClass().add(pane == genresPane ? "genre-chip" : "actor-chip");
            pane.getChildren().add(chip);
        }
    }

}