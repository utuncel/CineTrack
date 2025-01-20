package org.com.controller.user;

import org.com.models.user.User;
import org.com.repository.HibernateUtil;
import org.com.repository.UserDAO;
import org.com.service.SessionManagerService;

public class UserController {

  private final UserDAO userDAO;

  public UserController() {
    userDAO = new UserDAO(HibernateUtil.getSessionFactory());
  }

  public void registerUser(String username, String password) {
    User user = new User(username, password);

    try {
      if (userDAO.getUserByName(username) == null) {
        userDAO.createUser(user);
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to register user: " + e.getMessage(), e);
    }
  }

  public User authenticateUser(String username, String password) {
    try {
      User user = userDAO.authenticate(username, password);
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