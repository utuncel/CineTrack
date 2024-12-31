package org.com.models.user;

import java.util.List;
import org.com.models.Cinematic;
import org.com.models.logger.Logger;

public class User {

  private Long id;

  private String name;

  private String password;

  private List<Cinematic> cinematics;

  private List<Logger> logs;

  // Standardkonstruktor
  protected User() {}

  // Konstruktor
  public User(String name, String password) {
    this.name = name;
    this.password = password;
  }

  // Getter und Setter
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<Cinematic> getCinematics() {
    return cinematics;
  }

  public void setCinematics(List<Cinematic> cinematics) {
    this.cinematics = cinematics;
  }

  public List<Logger> getLogs() {
    return logs;
  }

  public void setLogs(List<Logger> logs) {
    this.logs = logs;
  }
}
