package org.com.controller.cinematics;

import java.util.List;
import javafx.fxml.FXML;
import org.com.controller.cinematics.helper.CinematicBoxController;
import org.com.controller.cinematics.helper.CinematicBoxListingController;
import org.com.controller.cinematics.helper.CinematicController;
import org.com.model.domain.Cinematic;
import org.com.model.enums.Type;

/**
 * Controller for handling Series-related cinematic data. Implements the {@link CinematicController}
 * interface to load and display a list of Cinematic objects of type Series in the UI. This class is
 * responsible for interacting with the {@link CinematicBoxListingController} to display the list of
 * Series Cinematics. A single instance you can see here {@link CinematicBoxController}.
 *
 * @author umut
 * @version 1.0
 * @see CinematicController
 * @see CinematicBoxListingController
 * @see CinematicBoxController
 */
public class SeriesController implements CinematicController {

  @FXML
  private CinematicBoxListingController cinematicListingController;

  /**
   * Loads a list of Cinematic objects and sets them to be displayed as Series Cinematics. This
   * method is called to populate the {@link CinematicBoxListingController} with Series data.
   *
   * @param cinematics A list of Cinematic objects of type Series to be loaded and displayed.
   */
  public void loadData(List<Cinematic> cinematics) {
    cinematicListingController.setCinematics(cinematics, Type.SERIES);
  }
}