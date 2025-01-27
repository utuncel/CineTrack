package org.com.controller.cinematics;

import java.util.List;
import javafx.fxml.FXML;
import org.com.controller.cinematics.helper.CinematicBoxController;
import org.com.controller.cinematics.helper.CinematicBoxListingController;
import org.com.controller.cinematics.helper.CinematicController;
import org.com.model.domain.Cinematic;
import org.com.model.enums.Type;

/**
 * Controller for handling Movie-related cinematic data. Implements the {@link CinematicController}
 * interface to load and display a list of Cinematic objects of type Movie in the UI. This class
 * interacts with the {@link CinematicBoxListingController} to display the list of Movie Cinematics.
 * A single instance you can see here {@link CinematicBoxController}.
 *
 * @author umut
 * @version 1.0
 * @see CinematicController
 * @see CinematicBoxListingController
 * @see CinematicBoxController
 */
public class MovieController implements CinematicController {

  @FXML
  private CinematicBoxListingController cinematicListingController;

  /**
   * Loads a list of Cinematic objects and sets them to be displayed as Movie Cinematics. This
   * method is called to populate the {@link CinematicBoxListingController} with Movie data.
   *
   * @param cinematics A list of Cinematic objects of type Movie to be loaded and displayed.
   */
  public void loadData(List<Cinematic> cinematics) {
    cinematicListingController.setCinematics(cinematics, Type.MOVIE);
  }
}