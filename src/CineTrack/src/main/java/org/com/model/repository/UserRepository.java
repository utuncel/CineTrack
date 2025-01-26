package org.com.model.repository;

import org.com.model.domain.User;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

/**
 * Repository for user-related database operations. Extends AbstractRepository to leverage standard
 * transaction handling.
 *
 * @author Umut
 * @version 1.0
 */
public class UserRepository extends AbstractRepository {

  /**
   * Constructs UserRepository with a Hibernate SessionFactory.
   *
   * @param sessionFactory Hibernate SessionFactory for database connections
   */
  public UserRepository(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  /**
   * Saves a new user to the database.
   *
   * @param user User to be persisted
   */
  public void saveUser(User user) {
    executeWithinTransaction(session -> {
      session.persist(user);
      return null;
    }, "Failed to save user");
  }

  /**
   * Retrieves a user by their username.
   *
   * @param username Username to search for
   * @return User with matching username or null
   */
  public User getUserByName(String username) {
    return executeWithinTransaction(session ->
            session.createQuery("FROM User u WHERE u.name = :name", User.class)
                .setParameter("name", username)
                .uniqueResult(),
        "Failed to retrieve user by username");
  }

  /**
   * Authenticates a user by username and password.
   *
   * @param username User's username
   * @param password User's password
   * @return Authenticated user or null if authentication fails
   */
  public User authenticateUser(String username, String password) {
    return executeWithinTransaction(session -> {
      Query<User> query = session.createQuery("FROM User u WHERE u.name = :name", User.class);
      query.setParameter("name", username);
      User user = query.uniqueResult();
      if (user != null && user.getPassword().equals(password)) {
        return user;
      }
      return null;
    }, "Authentication failed");
  }
}