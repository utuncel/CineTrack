package org.com.controller.addcinematic;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import org.com.controller.dashboard.DashboardModelSingleton;
import org.com.model.domain.Cinematic;
import org.com.model.domain.CsvCinematic;
import org.com.model.models.DashboardModel;
import org.com.repository.CinematicRepository;
import org.com.repository.HibernateUtil;
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
  private final DashboardModel dashboardModel = DashboardModelSingleton.getInstance();

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
  }

  @FXML
  public void initialize() {
    logger.logInfo("Initializing Add Cinematic Controller");
    try {
      ratingSpinner.setDisable(true);
      handleStateChange();
      logger.logInfo("Add Cinematic Controller successfully initialized");
    } catch (Exception e) {
      logger.logError("Error during Add Cinematic Controller initialization: " + e.getMessage());
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
      CsvCinematic csvCinematic = createCsvCinematic();
      Cinematic cinematic = cineFactoryService.createCinematic(csvCinematic);
      cinematicRepository.saveCinematic(cinematic, sessionManager.getCurrentUser());
      dashboardModel.addCinematic(cinematic);
    } catch (Exception e) {
      logger.logError("Error adding new cinematic: " + e.getMessage());
    }
  }

  private CsvCinematic createCsvCinematic() {
    CsvCinematic.Builder builder = new CsvCinematic.Builder().withTitle(titleField.getText())
        .withType(csvParserService.parseTypes(typeComboBox.getValue()))
        .withState(csvParserService.parseStates(stateComboBox.getValue()));

    if (!ignoreRating) {
      builder.withRating(ratingSpinner.getValue());
    }

    return builder.build();
  }
}