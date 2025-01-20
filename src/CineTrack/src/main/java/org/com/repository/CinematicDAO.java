package org.com.repository;

import org.com.models.Cinematic;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class CinematicDAO {
  private SessionFactory sessionFactory;

  public CinematicDAO(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public void createCinematics(List<Cinematic> cinematics) {
    if (cinematics == null || cinematics.isEmpty()) {
      throw new IllegalArgumentException("Die Liste der Cinematics darf nicht leer sein.");
    }

    Session session = null;
    Transaction transaction = null;
    try {
      session = sessionFactory.getCurrentSession();
      transaction = session.beginTransaction();

      for (Cinematic cinematic : cinematics) {
        if (cinematic.getUser() == null) {
          throw new IllegalArgumentException("Jedes Cinematic muss einem Benutzer zugeordnet sein.");
        }
        session.persist(cinematic);
      }

      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw new RuntimeException("Fehler beim Speichern der Cinematics-Liste", e);
    }
  }

  public List<Cinematic> getAllCinematicsByUser(Long userId) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery(
              "SELECT DISTINCT c FROM Cinematic c " +
                  "LEFT JOIN FETCH c.actors " +
                  "LEFT JOIN FETCH c.genres " +
                  "WHERE c.user.id = :userId", Cinematic.class)
          .setParameter("userId", userId)
          .list();

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public void deleteAllCinematicsByUser(Long userId) {
    Session session = null;
    Transaction transaction = null;
    try {
      session = sessionFactory.getCurrentSession();
      transaction = session.beginTransaction();

      // Alle Cinematics des Benutzers löschen
      session.createQuery("delete from Cinematic c where c.user.id = :userId")
          .setParameter("userId", userId)
          .executeUpdate();

      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw new RuntimeException("Fehler beim Löschen der Cinematics des Benutzers mit ID: " + userId, e);
    }
  }

}
