package org.com.service;

import javafx.scene.control.Alert;

/**
 * Service for creating and displaying JavaFX dialog alerts. Provides methods to show error and
 * information dialogs.
 *
 * @author Umut
 * @version 1.0
 */
public class DialogService {

  /**
   * Displays an error alert dialog.
   *
   * @param title   Title of the error dialog
   * @param content Detailed error message
   */
  public void showErrorAlert(String title, String content) {
    showAlert(Alert.AlertType.ERROR, title, content);
  }

  /**
   * Displays an information alert dialog.
   *
   * @param title   Title of the information dialog
   * @param content Detailed information message
   */
  public void showInfoAlert(String title, String content) {
    showAlert(Alert.AlertType.INFORMATION, title, content);
  }

  /**
   * Creates and shows a generic JavaFX alert dialog.
   *
   * @param type    Type of alert (error, information, etc.)
   * @param title   Title of the dialog
   * @param content Detailed message content
   */
  private void showAlert(Alert.AlertType type, String title, String content) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }
}