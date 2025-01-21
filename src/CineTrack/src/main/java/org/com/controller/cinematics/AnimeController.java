package org.com.controller.cinematics;

import java.util.List;
import javafx.fxml.FXML;
import org.com.controller.cinematics.helper.CinematicBoxListingController;
import org.com.controller.cinematics.helper.CinematicController;
import org.com.model.domain.Cinematic;
import org.com.model.enums.Type;

public class AnimeController implements CinematicController {

  @FXML
  private CinematicBoxListingController cinematicListingController;

  public void loadData(List<Cinematic> cinematics) {
    cinematicListingController.setCinematics(cinematics, Type.ANIME);
  }
}