package org.com.repository;

import org.com.models.user.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class UserRepository {

  private final SessionFactory sessionFactory;

  public UserRepository(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public void saveUser(User user) {
    executeWithinTransaction(session -> {
      session.persist(user);
      return null;
    }, "Failed to save user");
  }


  public User getUserByName(String username) {
    return executeWithinTransaction(session ->
            session.createQuery("FROM User u WHERE u.name = :name", User.class)
                .setParameter("name", username)
                .uniqueResult(),
        "Failed to retrieve user by username");
  }

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

  @FunctionalInterface
  private interface DatabaseOperation<T> {

    T execute(Session session);
  }

  public static class UserRepositoryException extends RuntimeException {

    public UserRepositoryException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
