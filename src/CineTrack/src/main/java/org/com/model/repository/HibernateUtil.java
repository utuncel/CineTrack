package org.com.model.repository;

import org.com.service.LogService;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

  private static final LogService logger = LogService.getInstance();
  private static final SessionFactory sessionFactory = buildSessionFactory();

  private HibernateUtil() {
  }

  private static SessionFactory buildSessionFactory() {
    try {
      String dbPassword = System.getenv("CT_DB_PASSWORD");
      if (dbPassword == null || dbPassword.isEmpty()) {
        logger.logError(
            "Database is not set");
        throw new IllegalArgumentException("Database is not set");
      }

      return new Configuration()
          .configure(
              HibernateUtil.class.getClassLoader().getResource("repository/hibernate.cfg.xml"))
          .setProperty("hibernate.connection.password", dbPassword)
          .buildSessionFactory();
    } catch (Exception e) {
      logger.logError(e.getMessage());
      throw new ExceptionInInitializerError(e);
    }
  }

  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }
}
