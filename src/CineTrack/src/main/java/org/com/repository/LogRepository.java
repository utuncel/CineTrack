package org.com.repository;

import java.util.List;
import org.com.model.domain.Log;
import org.com.model.domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class LogRepository {

  private final SessionFactory sessionFactory;

  public LogRepository(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public void saveLogger(Log log, User user) {
    executeWithinTransaction(session -> {
      log.setUser(user);
      session.persist(log);
      return null;
    }, "Failed to create log");
  }

  public List<Log> getLogsByUser(User user) {
    return executeWithinTransaction(session ->
            session.createQuery("FROM Log l WHERE l.user = :user", Log.class)
                .setParameter("authentication", user)
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
