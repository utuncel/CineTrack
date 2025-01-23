package org.com.controller.cinematics.helper;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import org.com.model.domain.Cinematic;
import org.com.model.enums.Type;
import org.com.service.LogService;

public class CinematicBoxListingController {

  private static final int PREFERRED_COLUMNS = 3;
  private static final int GAP = 20;

  private final Map<Cinematic, CinematicViewHolder> viewCache = new HashMap<>();
  private final LogService logger = LogService.getInstance();

  @FXML
  private TilePane cinematicsContainer;
  @FXML
  private TextField searchField;
  @FXML
  private ComboBox<SortOption> sortComboBox;

  private List<Cinematic> currentCinematics;
  private Type currentType;

  @FXML
  public void initialize() {
    configureCinematicsContainer();
    configureSortComboBox();
    configureSearchListener();
  }

  private void configureCinematicsContainer() {
    cinematicsContainer.setPrefColumns(PREFERRED_COLUMNS);
    cinematicsContainer.setHgap(GAP);
    cinematicsContainer.setVgap(GAP);
  }

  private void configureSortComboBox() {
    sortComboBox.setItems(FXCollections.observableArrayList(SortOption.values()));
    sortComboBox.setValue(SortOption.STANDARD);
    sortComboBox.valueProperty()
        .addListener((observable, oldValue, newValue) -> filterAndSortCinematics());
  }

  private void configureSearchListener() {
    searchField.textProperty()
        .addListener((observable, oldValue, newValue) -> filterAndSortCinematics());
  }

  public void setCinematics(List<Cinematic> cinematics, Type type) {
    clearCacheIfTypeChanged(type);

    this.currentType = type;
    this.currentCinematics = List.copyOf(cinematics);

    cacheMovieViews(cinematics);
    filterAndSortCinematics();
  }

  private void clearCacheIfTypeChanged(Type type) {
    if (this.currentType != type) {
      viewCache.clear();
    }
  }

  private void cacheMovieViews(List<Cinematic> cinematics) {
    cinematics.stream()
        .filter(cinematic -> !viewCache.containsKey(cinematic))
        .forEach(this::createAndCacheView);
  }

  private void createAndCacheView(Cinematic cinematic) {
    try {
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource("/cinematics/helper/CinematicBoxView.fxml"));
      StackPane cinematicPane = loader.load();
      CinematicBoxController controller = loader.getController();
      controller.setCinematicView(cinematic);

      viewCache.put(cinematic, new CinematicViewHolder(cinematicPane, controller));
    } catch (IOException e) {
      logger.logError("Error loading cinematic view: ");
    }
  }

  private void filterAndSortCinematics() {
    List<Cinematic> filteredCinematics = currentCinematics.stream()
        .filter(createTypeFilter())
        .filter(createSearchFilter())
        .sorted(createComparator())
        .toList();

    updateCinematicBoxes(filteredCinematics);
  }

  private Predicate<Cinematic> createTypeFilter() {
    return c -> c.getType() == currentType;
  }

  private Predicate<Cinematic> createSearchFilter() {
    return c -> searchField.getText().isEmpty() ||
        c.getTitle().toLowerCase().contains(searchField.getText().toLowerCase());
  }

  private Comparator<Cinematic> createComparator() {
    return switch (sortComboBox.getValue()) {
      case MY_RATING_HIGHEST -> Comparator.comparing(Cinematic::getMyRating).reversed();
      case MY_RATING_LOWEST -> Comparator.comparing(Cinematic::getMyRating);
      case IMDB_RATING_HIGHEST -> Comparator.comparing(Cinematic::getImdbRating).reversed();
      case IMDB_RATING_LOWEST -> Comparator.comparing(Cinematic::getImdbRating);
      case IMDB_VOTES_HIGHEST -> Comparator.comparing(Cinematic::getImdbVotes).reversed();
      case IMDB_VOTES_LOWEST -> Comparator.comparing(Cinematic::getImdbVotes);
      default -> (c1, c2) -> 0;
    };
  }

  private void updateCinematicBoxes(List<Cinematic> cinematics) {
    cinematicsContainer.getChildren().clear();
    cinematics.stream()
        .map(viewCache::get)
        .filter(Objects::nonNull)
        .forEach(viewHolder -> cinematicsContainer.getChildren().add(viewHolder.view()));
  }

  // Enum for sorting options to improve type safety and readability
  private enum SortOption {
    STANDARD("Standard"),
    MY_RATING_HIGHEST("MyRating (Highest first)"),
    MY_RATING_LOWEST("MyRating (Lowest first)"),
    IMDB_RATING_HIGHEST("ImdbRating (Highest first)"),
    IMDB_RATING_LOWEST("ImdbRating (Lowest first)"),
    IMDB_VOTES_HIGHEST("ImdbVotes (Highest first)"),
    IMDB_VOTES_LOWEST("ImdbVotes (Lowest first)");

    private final String displayName;

    SortOption(String displayName) {
      this.displayName = displayName;
    }

    @Override
    public String toString() {
      return displayName;
    }
  }

  private record CinematicViewHolder(StackPane view, CinematicBoxController controller) {

  }
}