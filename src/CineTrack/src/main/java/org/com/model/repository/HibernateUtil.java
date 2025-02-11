package org.com.model.repository;

import org.com.service.LogService;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utility class for creating and managing Hibernate SessionFactory.
 * <p>
 * Provides a singleton SessionFactory configured from a hibernate configuration file and uses an
 * environment variable for database password. You can find the config files under in the resources
 * folder.
 *
 * @author Umut
 * @version 1.1
 */
public class HibernateUtil {

  private static final LogService logger = LogService.getInstance();
  private static final SessionFactory sessionFactory = buildSessionFactory();

  /**
   * Private constructor to prevent instantiation.
   */
  private HibernateUtil() {
  }

  /**
   * Builds a SessionFactory using Hibernate configuration.
   *
   * @return Configured SessionFactory
   * @throws IllegalArgumentException    if database password is not set
   * @throws ExceptionInInitializerError if SessionFactory creation fails
   */
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
              HibernateUtil.class.getClassLoader().getResource("db_config/hibernate.cfg.xml"))
          .setProperty("hibernate.connection.password", dbPassword)
          .buildSessionFactory();
    } catch (Exception e) {
      logger.logError(e.getMessage());
      throw new ExceptionInInitializerError(e);
    }
  }

  /**
   * Retrieves the singleton SessionFactory.
   *
   * @return the configured SessionFactory
   */
  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }
}
