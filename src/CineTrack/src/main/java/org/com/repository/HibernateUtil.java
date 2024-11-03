package org.com.repository;

import org.com.models.Cinematic;
import org.com.models.logger.Logger;
import org.com.models.user.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class HibernateUtil {
  private static SessionFactory sessionFactory;

  public static SessionFactory getSessionFactory() {
    if (sessionFactory == null) {
      try {
        Configuration configuration = new Configuration();

        // Hibernate Einstellungen entsprechend einer properties Datei
        Properties settings = new Properties();

        // Datenbank-Treiber und Verbindung
        settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        settings.put(Environment.URL, "jdbc:mysql://your-oracle-cloud-endpoint:3306/your_database");
        settings.put(Environment.USER, "your_username");
        settings.put(Environment.PASS, "your_password");

        // Dialect für MySQL
        settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");

        // Echo SQL im Log
        settings.put(Environment.SHOW_SQL, "true");
        settings.put(Environment.FORMAT_SQL, "true");

        // Schema Update Strategie (update, create, create-drop, validate)
        settings.put(Environment.HBM2DDL_AUTO, "update");

        // Connection Pool Einstellungen
        settings.put(Environment.C3P0_MIN_SIZE, "5");
        settings.put(Environment.C3P0_MAX_SIZE, "20");
        settings.put(Environment.C3P0_TIMEOUT, "300");
        settings.put(Environment.C3P0_MAX_STATEMENTS, "50");
        settings.put(Environment.C3P0_IDLE_TEST_PERIOD, "3000");

        // Cache Einstellungen
        settings.put(Environment.USE_SECOND_LEVEL_CACHE, "true");
        settings.put(Environment.CACHE_REGION_FACTORY,
            "org.hibernate.cache.jcache.internal.JCacheRegionFactory");
        settings.put("hibernate.javax.cache.provider",
            "org.ehcache.jsr107.EhcacheCachingProvider");

        configuration.setProperties(settings);

        // Entities registrieren
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Cinematic.class);
        configuration.addAnnotatedClass(Logger.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
            .applySettings(configuration.getProperties())
            .build();

        sessionFactory = configuration.buildSessionFactory(serviceRegistry);

      } catch (Exception e) {
        e.printStackTrace();
        throw new ExceptionInInitializerError("Initial SessionFactory creation failed: " + e.getMessage());
      }
    }
    return sessionFactory;
  }

  /**
   * SessionFactory schließen
   */
  public static void shutdown() {
    if (sessionFactory != null) {
      sessionFactory.close();
    }
  }

  /**
   * Datenbank neu initialisieren (nur für Entwicklung/Tests)
   */
  public static void recreateDatabase() {
    try {
      Configuration configuration = new Configuration().configure();
      configuration.setProperty(Environment.HBM2DDL_AUTO, "create-drop");

      ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
          .applySettings(configuration.getProperties())
          .build();

      SessionFactory tempSessionFactory = configuration.buildSessionFactory(serviceRegistry);
      tempSessionFactory.close();

      // Zurück zu "update" für normale Operationen
      configuration.setProperty(Environment.HBM2DDL_AUTO, "update");

      serviceRegistry = new StandardServiceRegistryBuilder()
          .applySettings(configuration.getProperties())
          .build();

      sessionFactory = configuration.buildSessionFactory(serviceRegistry);

    } catch (Exception e) {
      e.printStackTrace();
      throw new ExceptionInInitializerError("Database recreation failed: " + e.getMessage());
    }
  }

  /**
   * Session Factory Status überprüfen
   */
  public static boolean isSessionFactoryOpen() {
    return sessionFactory != null && !sessionFactory.isClosed();
  }
}