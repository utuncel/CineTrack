package org.com.model.repository;

import org.com.model.domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 * Repository for user-related database operations using Hibernate. Handles user authentication,
 * retrieval, and persistence.
 *
 * @author Umut
 * @version 1.0
 */
public class UserRepository {

  private final SessionFactory sessionFactory;

  /**
   * Constructs UserRepository with a Hibernate SessionFactory.
   *
   * @param sessionFactory Hibernate SessionFactory for database connections
   */
  public UserRepository(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
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

  /**
   * Executes a database operation within a transaction.
   *
   * @param operation    Database operation to execute
   * @param errorMessage Error message for transaction failure
   * @return Result of the database operation
   * @throws UserRepositoryException If transaction fails
   */
  private <T> T executeWithinTransaction(DatabaseOperation<T> operation, String errorMessage) {
    Transaction transaction = null;
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();
      T result = operation.execute(session);
      transaction.commit();
      return result;
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw new UserRepositoryException(errorMessage, e);
    }
  }

  /**
   * Functional interface for database operations.
   *
   * @param <T> Type of operation result
   */
  @FunctionalInterface
  private interface DatabaseOperation<T> {

    T execute(Session session);
  }

  /**
   * Custom exception for user repository operations.
   */
  public static class UserRepositoryException extends RuntimeException {

    /**
     * Constructs a UserRepositoryException.
     *
     * @param message Error message
     * @param cause   Root cause of the exception
     */
    public UserRepositoryException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
