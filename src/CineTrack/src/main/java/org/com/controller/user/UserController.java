package org.com.controller.user;

import java.util.HashMap;
import java.util.Map;
import org.com.models.user.User;

public class UserController {
  private static final Map<String, User> userMap = new HashMap<>();

  public static void registerUser(String username, String password) {
    if (!userMap.containsKey(username)) {
      User newUser = new User(username, password);
      userMap.put(username, newUser);
    } else {
      throw new IllegalArgumentException("Username already exists.");
    }
  }

  public static User authenticateUser(String username, String password) {
    User user = userMap.get(username);
    if (user != null && user.validatePassword(password)) {
      return user;
    } else {
      return null;
    }
  }

  public static void updateUser(User user) {
    userMap.put(user.getName(), user);
  }

  public static User getUser(String username) {
    return userMap.get(username);
  }
}