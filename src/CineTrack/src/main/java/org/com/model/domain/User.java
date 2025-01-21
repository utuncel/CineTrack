package org.com.model.domain;

import java.util.List;

public class User {

  private Long id;

  private String name;

  private String password;

  private List<Cinematic> cinematics;

  private List<Log> logs;

  protected User() {
  }

  public User(String name, String password) {
    this.name = name;
    this.password = password;
  }

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

  public List<Log> getLogs() {
    return logs;
  }

  //not used but needed
  public void setLogs(List<Log> logs) {
    this.logs = logs;
  }
}
