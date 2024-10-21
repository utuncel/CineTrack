package org.com.controller.cinematics.helper;

import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import org.com.models.Cinematic;
import org.com.models.enums.Type;

public class CinematicBoxListingController {

  @FXML
  private TilePane cinematicsContainer;

  @FXML
  public void initialize() {
    // Set basic properties of TilePane
    cinematicsContainer.setPrefColumns(3);
    cinematicsContainer.setHgap(20);
    cinematicsContainer.setVgap(20);
  }

  public void setCinematics(List<Cinematic> cinematics, Type type) {
    // Filter cinematics by type
    List<Cinematic> filteredCinematics = cinematics.stream()
        .filter(c -> c.getType() == type)
        .toList();

    // Clear existing content
    cinematicsContainer.getChildren().clear();

    // Add new cinematic previews
    for (Cinematic cinematic : filteredCinematics) {
      try {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/Cinematics/Helper/CinematicBoxView.fxml"));
        AnchorPane cinematicPane = loader.load();

        CinematicBoxController controller = loader.getController();
        controller.setCinematicView(cinematic);

        cinematicsContainer.getChildren().add(cinematicPane);
      } catch (IOException e) {
        e.printStackTrace();
        System.err.println("Error loading cinematic view: " + e.getMessage());
      }
    }
  }
}