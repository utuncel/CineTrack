package org.com.controller.authentication;

import org.com.model.domain.User;
import org.com.repository.HibernateUtil;
import org.com.repository.UserRepository;
import org.com.service.LogService;
import org.com.service.SessionManagerService;

public class AuthenticationController {

  private final UserRepository userRepository;
  private final SessionManagerService sessionManager;
  private final LogService logger;

  public AuthenticationController() {
    this.userRepository = new UserRepository(HibernateUtil.getSessionFactory());
    this.sessionManager = SessionManagerService.getInstance();
    this.logger = LogService.getInstance();
  }

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