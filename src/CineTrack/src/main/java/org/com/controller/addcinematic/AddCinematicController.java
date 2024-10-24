package org.com.controller.addcinematic;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import org.com.controller.dashboard.DashboardModelSingleton;
import org.com.models.DashboardModel;
import org.com.models.helper.CsvCinematic;
import org.com.service.CineFactory;
import org.com.service.ParserUtil;

public class AddCinematicController {

  @FXML
  private TextField titleField;

  @FXML
  private ComboBox<String> typeComboBox;

  @FXML
  private ComboBox<String> stateComboBox;

  @FXML
  private Spinner<Integer> ratingSpinner;

  private DashboardModel dashboardModel;

  private ParserUtil parserUtil;

  private boolean ignoreRating = false;

  @FXML
  public void initialize() {
    // Set default state for rating field
    ratingSpinner.setDisable(true);
    dashboardModel = DashboardModelSingleton.getInstance();
    parserUtil = new ParserUtil();
    handleStateChange();
  }

  @FXML
  private void handleStateChange() {
    String selectedState = stateComboBox.getValue();
    ignoreRating = "TOWATCH".equals(selectedState);
    ratingSpinner.setDisable(ignoreRating);
  }

  @FXML
  private void handleAddCinematic() {

    CineFactory cineFactory = new CineFactory();

    CsvCinematic csvCinematic;

    if (ignoreRating) {
      csvCinematic = new CsvCinematic.Builder()
          .withTitle(titleField.getText())
          .withType(parserUtil.parseTypes(typeComboBox.getValue()))
          .withState(parserUtil.parseStates(stateComboBox.getValue()))
          .build();
    } else {
      csvCinematic = new CsvCinematic.Builder()
          .withTitle(titleField.getText())
          .withType(parserUtil.parseTypes(typeComboBox.getValue()))
          .withState(parserUtil.parseStates(stateComboBox.getValue()))
          .withRating(ratingSpinner.getValue())
          .build();

    }

    dashboardModel.addCinematic(cineFactory.createCinematic(csvCinematic));
  }
}