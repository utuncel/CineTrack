package org.com.models.logger;

import jakarta.persistence.*;
import org.com.models.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
public class Logger {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private LocalDateTime timestamp;

  @Column(nullable = false)
  private String level;

  @Column(nullable = false, length = 1000)
  private String message;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  // Standardkonstruktor für Hibernate
  protected Logger() {
    this.timestamp = LocalDateTime.now();
  }

  public Logger(String level, String message) {
    this.timestamp = LocalDateTime.now();
    this.level = level;
    this.message = message;
  }

  // Getter und Setter
  public Long getId() {
    return id;
  }

  protected void setId(Long id) {
    this.id = id;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  protected void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public String getLevel() {
    return level;
  }

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

  // Builder-Pattern für einfachere Erstellung
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

    public Logger build() {
      Logger logger = new Logger(level, message);
      logger.setUser(user);
      return logger;
    }
  }

  public static LoggerBuilder builder() {
    return new LoggerBuilder();
  }
}