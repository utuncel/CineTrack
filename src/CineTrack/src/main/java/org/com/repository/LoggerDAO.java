package org.com.repository;

import org.com.models.logger.Logger;
import org.com.models.user.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class LoggerDAO {
  private SessionFactory sessionFactory;

  public LoggerDAO(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public void createLogger(Logger logger, User user) {
    Session session = null;
    Transaction transaction = null;
    try {
      session = sessionFactory.getCurrentSession();
      transaction = session.beginTransaction();

      // Setze den User in den Logger
      logger.setUser(user);

      // Speichere den Logger
      session.persist(logger);

      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw new RuntimeException("Logger creation failed", e);
    }
  }

  public Logger getLoggerById(Long id) {
    try (Session session = sessionFactory.openSession()) {
      return session.get(Logger.class, id);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public List<Logger> getAllLoggers() {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("from Logger", Logger.class).list();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public void deleteLogger(Long id) {
    Session session = null;
    Transaction transaction = null;
    try {
      session = sessionFactory.getCurrentSession();
      transaction = session.beginTransaction();
      Logger logger = session.get(Logger.class, id);
      if (logger != null) {
        session.delete(logger);
      }
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      e.printStackTrace();
    }
  }
}
