package org.com.service;

import org.com.models.user.User;

public class SessionManagerService {

  private final LogService logger = LogService.getInstance();
  private User currentUser;

  private SessionManagerService() {
  }

  public static SessionManagerService getInstance() {
    return SessionHolder.INSTANCE;
  }

  public void setCurrentUser(User user) {
    if (user == null) {
      logger.logWarning("Attempt to set null user in session");
      throw new IllegalArgumentException("User cannot be null");
    }
    this.currentUser = user;
    logger.logInfo("User session started: " + user.getName());
  }

  public User getCurrentUser() {
    if (currentUser == null) {
      logger.logWarning("Attempt to get user from empty session");
      throw new IllegalStateException("No user currently logged in");
    }
    return currentUser;
  }

  private static final class SessionHolder {

    private static final SessionManagerService INSTANCE = new SessionManagerService();
  }
}