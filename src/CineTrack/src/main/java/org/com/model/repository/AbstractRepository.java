package org.com.model.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Abstract base class for repository implementations using Hibernate SessionFactory. Provides a
 * common constructor and session management for database-related repositories.
 *
 * <p>Subclasses must implement specific data access methods for their respective entities.</p>
 *
 * @author Umut
 * @version 1.0
 */
public abstract class AbstractRepository {

  protected final SessionFactory sessionFactory;

  /**
   * Constructs an AbstractRepository with the given Hibernate SessionFactory.
   *
   * @param sessionFactory The Hibernate SessionFactory used for creating database sessions
   */
  protected AbstractRepository(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  /**
   * Executes a database operation within a transaction.
   *
   * @param operation    Database operation to execute
   * @param errorMessage Error message for transaction failure
   * @return Result of the database operation
   * @throws RepositoryException If transaction fails
   */
  protected <T> T executeWithinTransaction(DatabaseOperation<T> operation, String errorMessage) {
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
      throw new RepositoryException(errorMessage, e);
    }
  }

  /**
   * Functional interface for database operations.
   *
   * @param <T> Type of operation result
   */
  @FunctionalInterface
  protected interface DatabaseOperation<T> {

    T execute(Session session);
  }

  /**
   * Custom exception for repository operations.
   */
  public static class RepositoryException extends RuntimeException {

    /**
     * Constructs a RepositoryException.
     *
     * @param message Error message
     * @param cause   Root cause of the exception
     */
    public RepositoryException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}