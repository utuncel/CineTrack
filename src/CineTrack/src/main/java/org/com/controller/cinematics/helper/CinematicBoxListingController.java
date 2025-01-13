package org.com.controller.cinematics.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import org.com.models.Cinematic;
import org.com.models.enums.Type;

public class CinematicBoxListingController {

  private final Map<Cinematic, CinematicViewHolder> viewCache = new HashMap<>();
  @FXML
  private TilePane cinematicsContainer;
  @FXML
  private TextField searchField;
  @FXML
  private ComboBox<String> sortComboBox;
  private List<Cinematic> currentCinematics = new ArrayList<>();
  private Type currentType;

  @FXML
  public void initialize() {
    cinematicsContainer.setPrefColumns(3);
    cinematicsContainer.setHgap(20);
    cinematicsContainer.setVgap(20);

    sortComboBox.setItems(FXCollections.observableArrayList(
        "Standard",
        "MyRating (Highest first)",
        "MyRating (Lowest first)",
        "ImdbRating (Highest first)",
        "ImdbRating (Lowest first)",
        "ImdbVotes (Highest first)",
        "ImdbVotes (Lowest first)"
    ));
    sortComboBox.setValue("Standard");

    searchField.textProperty()
        .addListener((observable, oldValue, newValue) -> filterAndSortCinematics());

    sortComboBox.valueProperty()
        .addListener((observable, oldValue, newValue) -> filterAndSortCinematics());
  }

  public void setCinematics(List<Cinematic> cinematics, Type type) {
    if (this.currentType != type) {
      viewCache.clear();
    }

    this.currentType = type;
    this.currentCinematics = new ArrayList<>(cinematics);

    for (Cinematic cinematic : cinematics) {
      if (!viewCache.containsKey(cinematic)) {
        try {
          FXMLLoader loader = new FXMLLoader(
              getClass().getResource("/cinematics/helper/CinematicBoxView.fxml"));
          StackPane cinematicPane = loader.load();
          CinematicBoxController controller = loader.getController();
          controller.setCinematicView(cinematic);

          viewCache.put(cinematic, new CinematicViewHolder(cinematicPane, controller));
        } catch (IOException e) {
          e.printStackTrace();
          System.err.println("Error loading cinematic view: " + e.getMessage());
        }
      }
    }

    filterAndSortCinematics();
  }

  private void filterAndSortCinematics() {
    List<Cinematic> filteredCinematics = currentCinematics.stream()
        .filter(c -> c.getType() == currentType)
        .filter(c -> searchField.getText().isEmpty() ||
            c.getTitle().toLowerCase().contains(searchField.getText().toLowerCase()))
        .sorted((c1, c2) -> {
          String sortOption = sortComboBox.getValue();
          if ("MyRating (Highest first)".equals(sortOption)) {
            return Double.compare(c2.getMyRating(), c1.getMyRating());
          } else if ("MyRating (Lowest first)".equals(sortOption)) {
            return Double.compare(c1.getMyRating(), c2.getMyRating());
          } else if ("ImdbRating (Highest first)".equals(sortOption)) {
            return Double.compare(c2.getImdbRating(), c1.getImdbRating());
          } else if ("ImdbRating (Lowest first)".equals(sortOption)) {
            return Double.compare(c1.getImdbRating(), c2.getImdbRating());
          } else if ("ImdbVotes (Highest first)".equals(sortOption)) {
            return Double.compare(c2.getImdbVotes(), c1.getImdbVotes());
          } else if ("ImdbVotes (Lowest first)".equals(sortOption)) {
            return Double.compare(c1.getImdbVotes(), c2.getImdbVotes());
          }
          return 0;
        })
        .toList();

    updateCinematicBoxes(filteredCinematics);
  }

  private void updateCinematicBoxes(List<Cinematic> cinematics) {
    cinematicsContainer.getChildren().clear();

    for (Cinematic cinematic : cinematics) {
      CinematicViewHolder viewHolder = viewCache.get(cinematic);
      if (viewHolder != null) {
        cinematicsContainer.getChildren().add(viewHolder.view);
      }
    }
  }

  private record CinematicViewHolder(StackPane view, CinematicBoxController controller) {

  }

}