package org.com.controller.addcinematic;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import org.com.model.models.DashboardModelSingleton;
import org.com.model.domain.Cinematic;
import org.com.model.domain.CsvCinematic;
import org.com.model.models.DashboardModel;
import org.com.model.repository.CinematicRepository;
import org.com.model.repository.HibernateUtil;
import org.com.service.CineFactoryService;
import org.com.service.CsvParserService;
import org.com.service.LogService;
import org.com.service.SessionManagerService;

public class AddCinematicController {

  private final LogService logger = LogService.getInstance();
  private final CsvParserService csvParserService = new CsvParserService();
  private final CineFactoryService cineFactoryService = new CineFactoryService();
  private final CinematicRepository cinematicRepository;
  private final SessionManagerService sessionManager;
  private final DashboardModel dashboardModel;

  @FXML
  private TextField titleField;
  @FXML
  private ComboBox<String> typeComboBox;
  @FXML
  private ComboBox<String> stateComboBox;
  @FXML
  private Spinner<Integer> ratingSpinner;

  private boolean ignoreRating = false;

  public AddCinematicController() {
    this.cinematicRepository = new CinematicRepository(HibernateUtil.getSessionFactory());
    this.sessionManager = SessionManagerService.getInstance();
    this.dashboardModel = DashboardModelSingleton.getInstance();
  }

  @FXML
  public void initialize() {
    try {
      ratingSpinner.setDisable(true);
      handleStateChange();
      logger.logInfo("Add Cinematic Controller initialized successfully");
    } catch (Exception e) {
      logger.logError("Initialization error: " + e.getMessage());
    }
  }

  @FXML
  private void handleStateChange() {
    ignoreRating = "TOWATCH".equals(stateComboBox.getValue());
    ratingSpinner.setDisable(ignoreRating);
  }

  @FXML
  private void handleAddCinematic() {
    try {
      Cinematic cinematic = processCinematic();
      dashboardModel.addCinematic(cinematic);
    } catch (Exception e) {
      logger.logError("Error adding cinematic: " + e.getMessage());
    }
  }

  private Cinematic processCinematic() {
    CsvCinematic csvCinematic = createCsvCinematic();
    Cinematic cinematic = cineFactoryService.createCinematic(csvCinematic);
    cinematicRepository.saveCinematic(cinematic, sessionManager.getCurrentUser());
    return cinematic;
  }

  private CsvCinematic createCsvCinematic() {
    CsvCinematic.Builder builder = new CsvCinematic.Builder()
        .withTitle(titleField.getText())
        .withType(csvParserService.parseTypes(typeComboBox.getValue()))
        .withState(csvParserService.parseStates(stateComboBox.getValue()));

    if (!ignoreRating) {
      builder.withRating(ratingSpinner.getValue());
    }

    return builder.build();
  }
}