package org.com.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.com.model.domain.ApiCinematic;
import org.com.model.domain.Cinematic;
import org.com.model.domain.CsvCinematic;
import org.com.model.domain.User;

/**
 * Factory service for creating Cinematic objects by combining CSV data with API information.
 * Handles the process of enriching CSV data with additional details from OMDb API.
 *
 * @author Umut
 * @version 1.0
 */
public class CineFactoryService {

  private final ApiService apiService;
  private final LogService logger = LogService.getInstance();
  private CsvImporterService csvImporterService;
  private User user;

  /**
   * Constructs CineFactoryService with CSV importer, API service, and user context.
   *
   * @param csvImporterService Service for importing data from CSV
   * @param apiService         Service for fetching movie/series data from API
   * @param user               User context for cinematic creation
   */
  public CineFactoryService(CsvImporterService csvImporterService, ApiService apiService,
      User user) {
    this.csvImporterService = csvImporterService;
    this.apiService = apiService;
    this.user = user;
    logger.logInfo("CineFactory initialized with CsvImporter and ApiData.");
  }

  /**
   * Constructs CineFactoryService for unit testing without user context.
   *
   * @param csvImporterService Service for importing data from CSV
   * @param apiService         Service for fetching movie/series data from API
   */
  public CineFactoryService(CsvImporterService csvImporterService, ApiService apiService) {
    this(csvImporterService, apiService, null);
    logger.logInfo("CineFactory initialized with CsvImporter and ApiData.");
  }

  /**
   * Default constructor creating a new ApiService instance.
   */
  public CineFactoryService() {
    this.apiService = new ApiService();
  }

  /**
   * Creates a list of Cinematic objects by processing CSV data and enriching with API information.
   *
   * @return List of fully created Cinematic objects
   * @throws IOException If error occurs during CSV import
   */
  public List<Cinematic> createCinematics() throws IOException {
    logger.logInfo("Starting to create cinematics from CSV data.");
    List<CsvCinematic> csvCinematics = csvImporterService.importData();
    List<Cinematic> createdCinematics = new ArrayList<>();
    List<String> notFoundTitles = new ArrayList<>();

    for (CsvCinematic csvCinematic : csvCinematics) {
      cinematicMerge(csvCinematic, createdCinematics, notFoundTitles);
    }

    handleNotFoundTitles(notFoundTitles);

    logger.logInfo("Finished creating cinematics. Total created: " + createdCinematics.size());
    return createdCinematics;
  }

  /**
   * Creates a single Cinematic object by combining CSV data with API information.
   *
   * @param csvCinematic CSV data for the cinematic
   * @return Fully created Cinematic object or null if no API data found
   */
  public Cinematic createCinematic(CsvCinematic csvCinematic) {
    ApiCinematic apiCinematic = apiService.fetchMoviesOrSeries(csvCinematic.getTitle());

    if (apiCinematic == null) {
      logWarningForTitleNotFound(csvCinematic.getTitle());
      return null;
    }

    return createCinematicFromApiAndCsv(apiCinematic, csvCinematic);
  }

  /**
   * Merges CSV cinematic data with API data and adds to created cinematics list.
   *
   * @param csvCinematic      CSV data for the cinematic
   * @param createdCinematics List of successfully created cinematics
   * @param notFoundTitles    List to track titles not found in API
   */
  private void cinematicMerge(CsvCinematic csvCinematic, List<Cinematic> createdCinematics,
      List<String> notFoundTitles) {
    ApiCinematic apiCinematic = apiService.fetchMoviesOrSeries(csvCinematic.getTitle());

    if (apiCinematic == null) {
      notFoundTitles.add(csvCinematic.getTitle());
      logWarningForTitleNotFound(csvCinematic.getTitle());
    } else {
      createdCinematics.add(createCinematicFromApiAndCsv(apiCinematic, csvCinematic));
      logger.logInfo("Added cinematic for title: " + csvCinematic.getTitle());
    }
  }

  /**
   * Logs warnings for titles that could not be found in the API.
   *
   * @param notFoundTitles List of titles not found in API
   */
  private void handleNotFoundTitles(List<String> notFoundTitles) {
    if (!notFoundTitles.isEmpty()) {
      for (String title : notFoundTitles) {
        logger.logWarning("- " + title);
      }
    }
  }

  /**
   * Creates a Cinematic object by combining API and CSV data with user context.
   *
   * @param apiCinematic API data for the cinematic
   * @param csvCinematic CSV data for the cinematic
   * @return Fully created Cinematic object
   */
  private Cinematic createCinematicFromApiAndCsv(ApiCinematic apiCinematic,
      CsvCinematic csvCinematic) {
    Cinematic cinematic = new Cinematic(apiCinematic, csvCinematic, this.user);
    logger.logInfo("Successfully created cinematic for title: " + csvCinematic.getTitle());
    return cinematic;
  }

  /**
   * Logs a warning for a title not found in the API.
   *
   * @param title Title that could not be found in API
   */
  private void logWarningForTitleNotFound(String title) {
    logger.logWarning("Cinematic data for title '" + title + "' not found in API.");
  }
}