package org.com.controller.cinematics.helper;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.com.model.domain.Cinematic;

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
    setupHoverEffects();
  }

  private void setupHoverEffects() {
    if (rootPane != null) {
      rootPane.setOnMouseEntered(e -> toggleOverlayVisibility(true));
      rootPane.setOnMouseExited(e -> toggleOverlayVisibility(false));
    }
  }

  private void toggleOverlayVisibility(boolean isVisible) {
    if (descriptionArea != null) {
      descriptionArea.setVisible(isVisible);
    }
    if (hoverOverlay != null) {
      hoverOverlay.setVisible(isVisible);
    }
  }

  public void setCinematicView(Cinematic cinematic) {
    if (cinematic == null) {
      return;
    }

    updatePoster(cinematic);
    updateLabels(cinematic);
    updateDescription(cinematic);
  }

  private void updatePoster(Cinematic cinematic) {
    if (posterImage != null && cinematic.getImageUrl() != null) {
      posterImage.setImage(new Image(cinematic.getImageUrl()));
    }
  }

  private void updateLabels(Cinematic cinematic) {
    updateLabel(titleLabel, cinematic.getTitle());
    updateLabel(runtimeLabel, "Runtime: " + cinematic.getRuntime());
    updateLabel(imdbRatingLabel, String.format("IMDb: %.1f/10 (%,d Stimmen)",
        cinematic.getImdbRating(), cinematic.getImdbVotes()));
    updateLabel(myRatingLabel, "MyRating: " + cinematic.getMyRating() + "/10");
    updateLabel(directorLabel, "Director: " + cinematic.getDirectorName());
    updateLabel(stateLabel, "State: " + cinematic.getState().toString());
  }

  private void updateLabel(Label label, String text) {
    if (label != null) {
      label.setText(text);
    }
  }

  private void updateDescription(Cinematic cinematic) {
    if (descriptionArea != null) {
      descriptionArea.setText(cinematic.getDescription());
    }
  }
}