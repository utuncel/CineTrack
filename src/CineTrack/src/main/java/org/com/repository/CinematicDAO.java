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

  public void createCinematic(Cinematic cinematic) {
    if (cinematic.getUser() == null) {
      throw new IllegalArgumentException("Cinematic must be associated with a user.");
    }

    Session session = null;
    Transaction transaction = null;
    try {
      session = sessionFactory.getCurrentSession();
      transaction = session.beginTransaction();
      session.persist(cinematic);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw new RuntimeException("Cinematic creation failed", e);
    }
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



  public Cinematic getCinematicById(Long id) {
    try (Session session = sessionFactory.openSession()) {
      return session.get(Cinematic.class, id);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public List<Cinematic> getAllCinematicsByUser(Long userId) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("from Cinematic c where c.user.id = :userId", Cinematic.class)
          .setParameter("userId", userId)
          .list();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }


  public void updateCinematic(Cinematic cinematic) {
    Session session = null;
    Transaction transaction = null;
    try {
      session = sessionFactory.getCurrentSession();
      transaction = session.beginTransaction();
      session.update(cinematic);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      e.printStackTrace();
    }
  }

  public void deleteCinematic(Long id) {
    Session session = null;
    Transaction transaction = null;
    try {
      session = sessionFactory.getCurrentSession();
      transaction = session.beginTransaction();
      Cinematic cinematic = session.get(Cinematic.class, id);
      if (cinematic != null) {
        session.delete(cinematic);
      }
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      e.printStackTrace();
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
