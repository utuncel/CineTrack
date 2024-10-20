package org.com.Controller.Cinematics;

import javafx.fxml.FXML;
import org.com.Controller.Cinematics.Helper.CinematicBoxListingController;
import org.com.Controller.Cinematics.Helper.CinematicController;
import org.com.Models.Cinematic;
import org.com.Models.Enums.Type;

import java.util.List;

public class MovieController implements CinematicController {

    @FXML
    private CinematicBoxListingController cinematicListingController;

    public void loadData(List<Cinematic> cinematics) {
        cinematicListingController.setCinematics(cinematics, Type.MOVIE);
    }
}