package org.com.service;

import org.com.model.domain.User;

/**
 * Singleton service managing user session state. Tracks the current logged-in user and provides
 * session management.
 *
 * @author Umut
 * @version 1.0
 */
public class SessionManagerService {

  private final LogService logger = LogService.getInstance();
  private User currentUser;

  private SessionManagerService() {
  }

  /**
   * Retrieves the singleton instance of SessionManagerService.
   *
   * @return Singleton SessionManagerService instance
   */
  public static SessionManagerService getInstance() {
    return SessionHolder.INSTANCE;
  }

  /**
   * Retrieves the currently logged-in user.
   *
   * @return Current user
   * @throws IllegalStateException If no user is logged in
   */
  public User getCurrentUser() {
    if (currentUser == null) {
      logger.logWarning("Attempt to get user from empty session");
      throw new IllegalStateException("No user currently logged in");
    }
    return currentUser;
  }

  /**
   * Sets the current user for the session.
   *
   * @param user User to be set as current
   * @throws IllegalArgumentException If user is null
   */
  public void setCurrentUser(User user) {
    if (user == null) {
      logger.logWarning("Attempt to set null user in session");
      throw new IllegalArgumentException("User cannot be null");
    }
    this.currentUser = user;
    logger.logInfo("User session started: " + user.getName());
  }

  /**
   * Nested static class implementing the singleton pattern's thread-safe lazy initialization. Holds
   * the single instance of SessionManagerService.
   */
  private static final class SessionHolder {

    private static final SessionManagerService INSTANCE = new SessionManagerService();
  }
}