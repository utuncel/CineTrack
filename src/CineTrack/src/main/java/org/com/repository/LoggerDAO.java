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

      logger.setUser(user);

      session.persist(logger);

      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw new RuntimeException("Logger creation failed", e);
    }
  }
}
