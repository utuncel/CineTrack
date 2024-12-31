package org.com.repository;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
  private static final SessionFactory sessionFactory
      = buildSessionFactory();
  private static SessionFactory buildSessionFactory()
  {
    try {
      // We need to create the SessionFactory from
      // hibernate.cfg.xml
      return new Configuration()
          .configure(HibernateUtil.class.getClassLoader().getResource(
              "persistence/hibernate.cfg.xml"))
          .setProperty("hibernate.connection.password", System.getenv("CT_DB_PASSWORD"))
          .buildSessionFactory();
    }
    catch (Throwable ex) {
      // Use a proper logging framework like SLF4J or Log4j
      System.err.println("SessionFactory creation failed: " + ex);
      throw new ExceptionInInitializerError(ex);
    }
  }
  public static SessionFactory getSessionFactory()
  {
    return sessionFactory;
  }
  public static void shutdown()
  {
    // Close caches and connection pools
    getSessionFactory().close();
  }
}