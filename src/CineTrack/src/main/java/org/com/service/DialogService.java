package org.com.service;

import javafx.scene.control.Alert;

public class DialogService {

  public void showErrorAlert(String title, String content) {
    showAlert(Alert.AlertType.ERROR, title, content);
  }

  public void showInfoAlert(String title, String content) {
    showAlert(Alert.AlertType.INFORMATION, title, content);
  }

  private void showAlert(Alert.AlertType type, String title, String content) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }
}