package org.com.model.repository;

import java.util.List;
import org.com.model.domain.Log;
import org.com.model.domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Repository for log-related database operations using Hibernate. Handles log persistence and
 * retrieval for users.
 *
 * @author Umut
 * @version 1.0
 */
public class LogRepository {

  private final SessionFactory sessionFactory;

  /**
   * Constructs LogRepository with a Hibernate SessionFactory.
   *
   * @param sessionFactory Hibernate SessionFactory for database connections
   */
  public LogRepository(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  /**
   * Saves a log entry associated with a user.
   *
   * @param log  Log to be persisted
   * @param user User associated with the log
   */
  public void saveLogger(Log log, User user) {
    executeWithinTransaction(session -> {
      log.setUser(user);
      session.persist(log);
      return null;
    }, "Failed to create log");
  }

  /**
   * Retrieves all logs for a specific user.
   *
   * @param user User whose logs are to be retrieved
   * @return List of logs for the user
   */
  public List<Log> getLogsByUser(User user) {
    return executeWithinTransaction(session ->
            session.createQuery("FROM Log l WHERE l.user = :user", Log.class)
                .setParameter("authentication", user)
                .list(),
        "Failed to retrieve logs for user");
  }

  /**
   * Executes a database operation within a transaction.
   *
   * @param operation    Database operation to execute
   * @param errorMessage Error message for transaction failure
   * @return Result of the database operation
   * @throws LoggerRepositoryException If transaction fails
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
      throw new LoggerRepositoryException(errorMessage, e);
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
   * Custom exception for log repository operations.
   */
  public static class LoggerRepositoryException extends RuntimeException {

    /**
     * Constructs a LoggerRepositoryException.
     *
     * @param message Error message
     * @param cause   Root cause of the exception
     */
    public LoggerRepositoryException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
