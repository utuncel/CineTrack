package org.com.controller.cinematics.helper;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.com.model.domain.Cinematic;

/**
 * Controller for displaying a Cinematic box element. Manages the display and organization of
 * Cinematic details such as the title, poster, description, ratings, and other related information.
 * Also handles hover effects to show additional information in the UI.
 *
 * @author umut
 * @version 1.0
 * @see org.com.model.domain.Cinematic
 */
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

  /**
   * Initializes the controller and sets up hover effects for the box element. This is automatically
   * called when the FXML file is loaded.
   */
  @FXML
  public void initialize() {
    setupHoverEffects();
  }

  /**
   * Sets up hover effects for the root pane to control the visibility of the overlay.
   */
  private void setupHoverEffects() {
    if (rootPane != null) {
      rootPane.setOnMouseEntered(e -> toggleOverlayVisibility(true));
      rootPane.setOnMouseExited(e -> toggleOverlayVisibility(false));
    }
  }

  /**
   * Toggles the visibility of the overlay elements (description and overlay) based on the provided
   * {@code isVisible} parameter.
   *
   * @param isVisible Indicates whether the overlay should be visible or not.
   */
  private void toggleOverlayVisibility(boolean isVisible) {
    if (descriptionArea != null) {
      descriptionArea.setVisible(isVisible);
    }
    if (hoverOverlay != null) {
      hoverOverlay.setVisible(isVisible);
    }
  }

  /**
   * Sets the information of a Cinematic object into the respective UI elements.
   *
   * @param cinematic The Cinematic object whose data is to be displayed.
   */
  public void setCinematicView(Cinematic cinematic) {
    if (cinematic == null) {
      return;
    }

    updatePoster(cinematic);
    updateLabels(cinematic);
    updateDescription(cinematic);
  }

  /**
   * Updates the poster image of the Cinematic in the ImageView.
   *
   * @param cinematic The Cinematic object whose poster image is to be displayed.
   */
  private void updatePoster(Cinematic cinematic) {
    if (posterImage != null && cinematic.getImageUrl() != null) {
      posterImage.setImage(new Image(cinematic.getImageUrl()));
    }
  }

  /**
   * Updates the text labels with the information of the Cinematic object.
   *
   * @param cinematic The Cinematic object whose data is used to update the labels.
   */
  private void updateLabels(Cinematic cinematic) {
    updateLabel(titleLabel, cinematic.getTitle());
    updateLabel(runtimeLabel, "Runtime: " + cinematic.getRuntime());
    updateLabel(imdbRatingLabel, String.format("IMDb: %.1f/10 (%,d Stimmen)",
        cinematic.getImdbRating(), cinematic.getImdbVotes()));
    updateLabel(myRatingLabel, "MyRating: " + cinematic.getMyRating() + "/10");
    updateLabel(directorLabel, "Director: " + cinematic.getDirectorName());
    updateLabel(stateLabel, "State: " + cinematic.getState().toString());
  }

  /**
   * Helper method to update the text of a label.
   *
   * @param label The label whose text is to be updated.
   * @param text  The new text to be displayed in the label.
   */
  private void updateLabel(Label label, String text) {
    if (label != null) {
      label.setText(text);
    }
  }

  /**
   * Sets the description of the Cinematic in the TextArea element.
   *
   * @param cinematic The Cinematic object whose description is to be displayed.
   */
  private void updateDescription(Cinematic cinematic) {
    if (descriptionArea != null) {
      descriptionArea.setText(cinematic.getDescription());
    }
  }
}