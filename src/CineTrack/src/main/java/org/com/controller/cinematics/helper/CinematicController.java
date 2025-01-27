package org.com.controller.cinematics.helper;

import java.util.List;
import org.com.model.domain.Cinematic;

/**
 * Interface for managing the loading of Cinematic data in controllers. Implementing classes should
 * define how to load and handle the display of a list of Cinematics.
 *
 * @author umut
 * @version 1.0
 * @see org.com.model.domain.Cinematic
 */
public interface CinematicController {

  /**
   * Loads a list of Cinematic objects into the controller. The implementation of this method will
   * define how the Cinematics are processed and displayed.
   *
   * @param cinematics A list of Cinematic objects to load.
   */
  void loadData(List<Cinematic> cinematics);
}
