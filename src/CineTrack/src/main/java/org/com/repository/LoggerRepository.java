package org.com.repository;

import java.util.List;
import org.com.models.logger.Logger;
import org.com.models.user.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class LoggerRepository {

  private final SessionFactory sessionFactory;

  public LoggerRepository(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public void saveLogger(Logger logger, User user) {
    executeWithinTransaction(session -> {
      logger.setUser(user);
      session.persist(logger);
      return null;
    }, "Failed to create logger");
  }

  public List<Logger> getLogsByUser(User user) {
    return executeWithinTransaction(session ->
            session.createQuery("FROM Logger l WHERE l.user = :user", Logger.class)
                .setParameter("user", user)
                .list(),
        "Failed to retrieve logs for user");
  }

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

  @FunctionalInterface
  private interface DatabaseOperation<T> {

    T execute(Session session);
  }

  public static class LoggerRepositoryException extends RuntimeException {

    public LoggerRepositoryException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
