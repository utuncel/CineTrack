package org.com.controller.addcinematic;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import org.com.model.domain.Cinematic;
import org.com.model.domain.CsvCinematic;
import org.com.model.models.CinematicModel;
import org.com.model.models.DashboardModelSingleton;
import org.com.model.repository.CinematicRepository;
import org.com.model.repository.HibernateUtil;
import org.com.service.CineFactoryService;
import org.com.service.CsvParserService;
import org.com.service.LogService;
import org.com.service.SessionManagerService;

/**
 * Controller class responsible for handling the addition of new cinematics (movies, series, or
 * anime) to the application. This class manages the user input, validates it, and integrates it
 * into the application model and database.
 *
 * <p>Dependencies include:</p>
 * <ul>
 *   <li>{@link LogService} for logging application events and errors.</li>
 *   <li>{@link CsvParserService} for parsing user input into valid states and types.</li>
 *   <li>{@link CineFactoryService} for creating {@link Cinematic} objects from parsed input.</li>
 *   <li>{@link CinematicRepository} for saving cinematics to the database.</li>
 *   <li>{@link SessionManagerService} for managing user sessions.</li>
 *   <li>{@link CinematicModel} for updating the application model with new cinematics.</li>
 * </ul>
 *
 * @author umut
 * @version 1.0
 */
public class AddCinematicController {

  private final LogService logger = LogService.getInstance();
  private final CsvParserService csvParserService = new CsvParserService();
  private final CineFactoryService cineFactoryService = new CineFactoryService();
  private final CinematicRepository cinematicRepository;
  private final SessionManagerService sessionManager;
  private final CinematicModel cinematicModel;

  @FXML
  private TextField titleField;
  @FXML
  private ComboBox<String> typeComboBox;
  @FXML
  private ComboBox<String> stateComboBox;
  @FXML
  private Spinner<Integer> ratingSpinner;

  private boolean ignoreRating = false;

  /**
   * Constructs an {@code AddCinematicController} and initializes the required services and
   * repositories.
   */
  public AddCinematicController() {
    this.cinematicRepository = new CinematicRepository(HibernateUtil.getSessionFactory());
    this.sessionManager = SessionManagerService.getInstance();
    this.cinematicModel = DashboardModelSingleton.getInstance();
  }

  /**
   * Initializes the controller, setting up UI components and logging successful initialization.
   */
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

  /**
   * Handles changes to the state of a cinematic. Disables the rating spinner if the state is
   * "TOWATCH".
   */
  @FXML
  private void handleStateChange() {
    ignoreRating = "TOWATCH".equals(stateComboBox.getValue());
    ratingSpinner.setDisable(ignoreRating);
  }

  /**
   * Handles the addition of a new cinematic based on user input. Validates the input, creates the
   * cinematic object, saves it to the database, and updates the dashboard model.
   */
  @FXML
  private void handleAddCinematic() {
    try {
      Cinematic cinematic = processCinematic();
      cinematicModel.addCinematic(cinematic);
    } catch (Exception e) {
      logger.logError("Error adding cinematic: " + e.getMessage());
    }
  }

  /**
   * Processes the user input to create a {@link Cinematic} object, save it to the database, and
   * return it for further use.
   *
   * @return the created {@link Cinematic} object.
   * @throws RuntimeException if any error occurs during the process.
   */
  private Cinematic processCinematic() {
    CsvCinematic csvCinematic = createCsvCinematic();
    Cinematic cinematic = cineFactoryService.createCinematic(csvCinematic);
    cinematicRepository.saveCinematic(cinematic, sessionManager.getCurrentUser());
    return cinematic;
  }

  /**
   * Creates a {@link CsvCinematic} object based on the user input. Parses input fields and
   * validates them before building the object.
   *
   * @return the built {@link CsvCinematic} object.
   */
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