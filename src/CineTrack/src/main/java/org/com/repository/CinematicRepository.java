package org.com.repository;

import java.util.List;
import org.com.model.domain.Cinematic;
import org.com.model.domain.User;
import org.hibernate.HibernateError;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class CinematicRepository {

  private final SessionFactory sessionFactory;

  public CinematicRepository(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public void saveCinematic(Cinematic cinematic, User user) {
    if (cinematic == null) {
      throw new IllegalArgumentException("Cinematic cannot be null.");
    }

    executeWithinTransaction(session -> {
      if (cinematic.getUser() == null) {
        cinematic.setUser(user);  // Associate cinematic with the user if not already set
      }
      session.persist(cinematic);
      return null;
    }, "Failed to save the cinematic");
  }


  public void saveCinematics(List<Cinematic> cinematics, User user) {
    if (cinematics == null || cinematics.isEmpty()) {
      throw new IllegalArgumentException("The list of cinematics cannot be empty.");
    }

    executeWithinTransaction(session -> {
      for (Cinematic cinematic : cinematics) {
        if (cinematic.getUser() == null) {
          cinematic.setUser(user);  // Associate cinematic with the user if not already set
        }
        session.persist(cinematic);
      }
      return null;
    }, "Failed to save the cinematics list");
  }

  public List<Cinematic> getAllCinematicsByUser(User user) {
    return executeWithinTransaction(session ->
            session.createQuery(
                    "SELECT DISTINCT c FROM Cinematic c " +
                        "LEFT JOIN FETCH c.actors " +
                        "LEFT JOIN FETCH c.genres " +
                        "WHERE c.user = :user", Cinematic.class)
                .setParameter("user", user)
                .list(),
        "Failed to fetch cinematics for the user");
  }

  public void deleteAllCinematicsByUser(User user) {
    executeWithinTransaction(session -> {
      session.createMutationQuery("DELETE FROM Cinematic c WHERE c.user = :user")
          .setParameter("user", user)
          .executeUpdate();
      return null;
    }, "Failed to delete cinematics for the user");
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
      throw new HibernateError(errorMessage, e);
    }
  }

  @FunctionalInterface
  private interface DatabaseOperation<T> {

    T execute(Session session);
  }
}
