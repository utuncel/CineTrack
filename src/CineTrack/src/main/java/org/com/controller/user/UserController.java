package org.com.controller.user;

import org.com.model.domain.User;
import org.com.repository.HibernateUtil;
import org.com.repository.UserRepository;
import org.com.service.SessionManagerService;

public class UserController {

  private final UserRepository userRepository;

  public UserController() {
    userRepository = new UserRepository(HibernateUtil.getSessionFactory());
  }

  public void registerUser(String username, String password) {
    User user = new User(username, password);

    try {
      if (userRepository.getUserByName(username) == null) {
        userRepository.saveUser(user);
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to register user: " + e.getMessage(), e);
    }
  }

  public User authenticateUser(String username, String password) {
    try {
      User user = userRepository.authenticateUser(username, password);
      if (user != null) {
        SessionManagerService.getInstance().setCurrentUser(user);
        return user;
      }
    } catch (Exception e) {
      throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
    }
    return null;
  }
}