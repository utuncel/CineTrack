package org.com.controller.authentication;

import org.com.model.domain.User;
import org.com.model.repository.HibernateUtil;
import org.com.model.repository.UserRepository;
import org.com.service.LogService;
import org.com.service.SessionManagerService;

/**
 * Controller for managing user authentication and registration. Provides functionality to register
 * new users and authenticate existing users. Handles session management and logs important events
 * during the authentication process.
 * <p>
 * This class interacts with the {@link UserRepository} for database operations, the
 * {@link SessionManagerService} for managing the current user session, and the {@link LogService}
 * for logging events and errors.
 *
 * @author umut
 * @version 1.0
 * @see UserRepository
 * @see SessionManagerService
 * @see LogService
 */
public class AuthenticationController {

  private final UserRepository userRepository;
  private final SessionManagerService sessionManager;
  private final LogService logger;

  /**
   * Constructs an {@code AuthenticationController} and initializes dependencies for user
   * repository, session management, and logging.
   */
  public AuthenticationController() {
    this.userRepository = new UserRepository(HibernateUtil.getSessionFactory());
    this.sessionManager = SessionManagerService.getInstance();
    this.logger = LogService.getInstance();
  }

  /**
   * Registers a new user with the specified username and password.
   *
   * @param username The username of the user to be registered.
   * @param password The password of the user to be registered.
   * @return {@code true} if the user was successfully registered, {@code false} if the username
   * already exists or if an error occurred.
   */
  public boolean registerUser(String username, String password) {
    try {
      if (userRepository.getUserByName(username) == null) {
        User user = new User(username, password);
        userRepository.saveUser(user);
        logger.logInfo("User registered successfully: " + username);
        return true;
      }
      logger.logWarning("Registration failed: Username already exists");
      return false;
    } catch (Exception e) {
      logger.logError("User registration failed: " + e.getMessage());
      return false;
    }
  }

  /**
   * Authenticates a user with the provided username and password.
   *
   * @param username The username of the user attempting to authenticate.
   * @param password The password of the user attempting to authenticate.
   * @return The authenticated {@link User} object if authentication is successful, or {@code null}
   * if authentication fails or an error occurs.
   */
  public User authenticateUser(String username, String password) {
    try {
      User user = userRepository.authenticateUser(username, password);
      if (user != null) {
        sessionManager.setCurrentUser(user);
        logger.logInfo("User authenticated successfully: " + username);
        return user;
      }
      logger.logWarning("Authentication failed for user: " + username);
      return null;
    } catch (Exception e) {
      logger.logError("Authentication error: " + e.getMessage());
    }
    return null;
  }
}