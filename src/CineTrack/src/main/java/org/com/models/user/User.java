package org.com.models.user;

import jakarta.persistence.*;
import org.com.models.Cinematic;
import org.com.models.logger.Logger;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private String password;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Cinematic> cinematics = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Logger> logs = new ArrayList<>();

  // Standardkonstruktor für Hibernate
  protected User() {}

  public User(String name, String password) {
    this.name = name;
    this.password = password;
  }

  // Getter und Setter für id
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

  public List<Cinematic> getCinematicsList() {
    return cinematics;
  }

  public void addCinematic(Cinematic cinematic) {
    cinematics.add(cinematic);
    cinematic.setUser(this);
  }

  public void removeCinematic(Cinematic cinematic) {
    cinematics.remove(cinematic);
    cinematic.setUser(null);
  }

  public List<Logger> getLogs() {
    return logs;
  }

  public void addLog(Logger log) {
    logs.add(log);
    log.setUser(this);
  }

  public boolean validatePassword(String password) {
    return this.password.equals(password);
  }

  public void changePassword(String newPassword) {
    this.password = newPassword;
  }
}