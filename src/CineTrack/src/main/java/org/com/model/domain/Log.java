package org.com.model.domain;


import java.time.LocalDateTime;

public class Log {

  private Long id;

  private LocalDateTime timestamp;

  private String level;

  private String message;

  private User user;

  protected Log() {
    this.timestamp = LocalDateTime.now();
  }

  public Log(String level, String message) {
    this.timestamp = LocalDateTime.now();
    this.level = level;
    this.message = message;
  }

  public Long getId() {
    return id;
  }

  //not used but needed
  protected void setId(Long id) {
    this.id = id;
  }

  //not used but needed
  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  //not used but needed
  protected void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public String getLevel() {
    return level;
  }

  //not used but needed
  public void setLevel(String level) {
    this.level = level;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public static class LoggerBuilder {
    private String level;
    private String message;
    private User user;

    public LoggerBuilder level(String level) {
      this.level = level;
      return this;
    }

    public LoggerBuilder message(String message) {
      this.message = message;
      return this;
    }

    public LoggerBuilder user(User user) {
      this.user = user;
      return this;
    }

    public Log build() {
      Log log = new Log(level, message);
      log.setUser(user);
      return log;
    }
  }

  public static LoggerBuilder builder() {
    return new LoggerBuilder();
  }
}