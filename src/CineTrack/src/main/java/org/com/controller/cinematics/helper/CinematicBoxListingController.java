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

/**
 * Controller for managing the listing of Cinematic objects in a grid format. This controller
 * handles the sorting, filtering, and search functionality for displaying Cinematic items based on
 * their type and user preferences. It also manages the caching of views for each Cinematic to
 * optimize performance. A single instance you can see here {@link CinematicBoxController}.
 *
 * @author umut
 * @version 1.0
 */
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

  /**
   * Initializes the controller, configuring the UI components such as the Cinematic container, the
   * sort combo box, and the search field listener.
   */
  @FXML
  public void initialize() {
    configureCinematicsContainer();
    configureSortComboBox();
    configureSearchListener();
  }

  /**
   * Configures the cinematics container to have a fixed number of columns and defined gaps.
   */
  private void configureCinematicsContainer() {
    cinematicsContainer.setPrefColumns(PREFERRED_COLUMNS);
    cinematicsContainer.setHgap(GAP);
    cinematicsContainer.setVgap(GAP);
  }

  /**
   * Configures the sort combo box with available sorting options and sets the default value.
   */
  private void configureSortComboBox() {
    sortComboBox.setItems(FXCollections.observableArrayList(SortOption.values()));
    sortComboBox.setValue(SortOption.STANDARD);
    sortComboBox.valueProperty()
        .addListener((observable, oldValue, newValue) -> filterAndSortCinematics());
  }

  /**
   * Sets up a listener for changes in the search field to filter and sort the Cinematic list
   * dynamically.
   */
  private void configureSearchListener() {
    searchField.textProperty()
        .addListener((observable, oldValue, newValue) -> filterAndSortCinematics());
  }

  /**
   * Sets the list of Cinematics and their type to be displayed, caches their views, and triggers
   * filtering and sorting.
   *
   * @param cinematics The list of Cinematics to display.
   * @param type       The type of Cinematics (e.g., MOVIE, SERIES, etc.).
   */
  public void setCinematics(List<Cinematic> cinematics, Type type) {
    clearCacheIfTypeChanged(type);

    this.currentType = type;
    this.currentCinematics = List.copyOf(cinematics);

    cacheMovieViews(cinematics);
    filterAndSortCinematics();
  }

  /**
   * Clears the view cache if the Cinematic type has changed.
   *
   * @param type The new Cinematic type.
   */
  private void clearCacheIfTypeChanged(Type type) {
    if (this.currentType != type) {
      viewCache.clear();
    }
  }

  /**
   * Caches the views for each Cinematic object by loading and storing them in a map.
   *
   * @param cinematics The list of Cinematics to cache views for.
   */
  private void cacheMovieViews(List<Cinematic> cinematics) {
    cinematics.stream()
        .filter(cinematic -> !viewCache.containsKey(cinematic))
        .forEach(this::createAndCacheView);
  }

  /**
   * Creates and caches a view for a Cinematic object.
   *
   * @param cinematic The Cinematic object for which to create a view.
   */
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

  /**
   * Filters and sorts the list of Cinematics based on the selected search term and sorting option,
   * and updates the display.
   */
  private void filterAndSortCinematics() {
    List<Cinematic> filteredCinematics = currentCinematics.stream()
        .filter(createTypeFilter())
        .filter(createSearchFilter())
        .sorted(createComparator())
        .toList();

    updateCinematicBoxes(filteredCinematics);
  }

  /**
   * Creates a filter to match Cinematics of the current type.
   *
   * @return A predicate for filtering Cinematics by type.
   */
  private Predicate<Cinematic> createTypeFilter() {
    return c -> c.getType() == currentType;
  }

  /**
   * Creates a filter to match Cinematics based on the search text.
   *
   * @return A predicate for filtering Cinematics by search text.
   */
  private Predicate<Cinematic> createSearchFilter() {
    return c -> searchField.getText().isEmpty() ||
        c.getTitle().toLowerCase().contains(searchField.getText().toLowerCase());
  }

  /**
   * Creates a comparator based on the selected sorting option.
   *
   * @return A comparator for sorting Cinematics.
   */
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

  /**
   * Updates the display of Cinematic views in the container.
   *
   * @param cinematics The list of Cinematics to display.
   */
  private void updateCinematicBoxes(List<Cinematic> cinematics) {
    cinematicsContainer.getChildren().clear();
    cinematics.stream()
        .map(viewCache::get)
        .filter(Objects::nonNull)
        .forEach(viewHolder -> cinematicsContainer.getChildren().add(viewHolder.view()));
  }

  /**
   * Enum representing the sorting options for Cinematics. Provides a user-friendly string
   * representation for each sorting option.
   */
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

  /**
   * A simple container to hold a Cinematic view and its associated controller. This is used to
   * cache the views for Cinematics to optimize performance.
   */
  private record CinematicViewHolder(StackPane view, CinematicBoxController controller) {

  }
}