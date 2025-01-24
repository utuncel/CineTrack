package org.com.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.com.model.domain.ApiCinematic;
import org.com.model.domain.Cinematic;
import org.com.model.domain.CsvCinematic;
import org.com.model.domain.User;

public class CineFactoryService {

  private final ApiService apiService;
  private final LogService logger = LogService.getInstance();
  private CsvImporterService csvImporterService;
  private User user;

  public CineFactoryService(CsvImporterService csvImporterService, ApiService apiService,
      User user) {
    this.csvImporterService = csvImporterService;
    this.apiService = apiService;
    this.user = user;
    logger.logInfo("CineFactory initialized with CsvImporter and ApiData.");
  }

  // Constructor for unit tests
  public CineFactoryService(CsvImporterService csvImporterService, ApiService apiService) {
    this(csvImporterService, apiService, null);
    logger.logInfo("CineFactory initialized with CsvImporter and ApiData.");
  }

  public CineFactoryService() {
    this.apiService = new ApiService();
  }

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

  public Cinematic createCinematic(CsvCinematic csvCinematic) {
    ApiCinematic apiCinematic = apiService.fetchMoviesOrSeries(csvCinematic.getTitle());

    if (apiCinematic == null) {
      logWarningForTitleNotFound(csvCinematic.getTitle());
      return null;
    }

    return createCinematicFromApiAndCsv(apiCinematic, csvCinematic);
  }

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

  private void handleNotFoundTitles(List<String> notFoundTitles) {
    if (!notFoundTitles.isEmpty()) {
      for (String title : notFoundTitles) {
        logger.logWarning("- " + title);
      }
    }
  }

  private Cinematic createCinematicFromApiAndCsv(ApiCinematic apiCinematic,
      CsvCinematic csvCinematic) {
    Cinematic cinematic = new Cinematic(apiCinematic, csvCinematic, this.user);
    logger.logInfo("Successfully created cinematic for title: " + csvCinematic.getTitle());
    return cinematic;
  }

  private void logWarningForTitleNotFound(String title) {
    logger.logWarning("Cinematic data for title '" + title + "' not found in API.");
  }
}