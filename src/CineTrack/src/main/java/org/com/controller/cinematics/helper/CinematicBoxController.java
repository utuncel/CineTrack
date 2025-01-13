package org.com.controller.cinematics.helper;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.com.models.Cinematic;
import javafx.scene.layout.StackPane;

public class CinematicBoxController {

  @FXML
  private TextArea descriptionArea;
  @FXML
  private Label directorLabel;
  @FXML
  private Label runtimeLabel;
  @FXML
  private ImageView posterImage;
  @FXML
  private Label imdbRatingLabel;
  @FXML
  private Label myRatingLabel;
  @FXML
  private Label titleLabel;
  @FXML
  private Label stateLabel;
  @FXML
  private VBox hoverOverlay;
  @FXML
  private StackPane rootPane;

  @FXML
  public void initialize() {
    // Setup hover effects
    if (rootPane != null) {
      rootPane.setOnMouseEntered(e -> {
        if (descriptionArea != null) descriptionArea.setVisible(true);
        if (hoverOverlay != null) hoverOverlay.setVisible(true);
      });

      rootPane.setOnMouseExited(e -> {
        if (descriptionArea != null) descriptionArea.setVisible(false);
        if (hoverOverlay != null) hoverOverlay.setVisible(false);
      });
    }
  }

  public void setCinematicView(Cinematic cinematic) {
    if (cinematic == null) return;

    // Set image
    if (posterImage != null && cinematic.getImageUrl() != null) {
      posterImage.setImage(new Image(cinematic.getImageUrl()));
    }

    // Set text labels
    if (titleLabel != null) {
      titleLabel.setText(cinematic.getTitle());
    }

    if (runtimeLabel != null) {
      runtimeLabel.setText("Runtime: " + cinematic.getRuntime());
    }

    if (imdbRatingLabel != null) {
      imdbRatingLabel.setText(String.format("IMDb: %.1f/10 (%,d Stimmen)",
          cinematic.getImdbRating(), cinematic.getImdbVotes()));
    }

    if (myRatingLabel != null) {
      myRatingLabel.setText("MyRating: " + cinematic.getMyRating() + "/10");
    }

    if (directorLabel != null) {
      directorLabel.setText("Director: " + cinematic.getDirectorName());
    }

    if (stateLabel != null) {
      stateLabel.setText("State: " + cinematic.getState().toString());
    }

    if (descriptionArea != null) {
      descriptionArea.setText(cinematic.getDescription());
    }
  }
}