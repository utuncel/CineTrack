package junit;

import org.com.models.logger.Logger;
import org.com.models.user.User;
import org.com.repository.HibernateUtil;
import org.com.repository.LoggerDAO;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoggerDAOTest {
  private SessionFactory sessionFactory;
  private LoggerDAO loggerDAO;

  @BeforeAll
  void setUp() {
    sessionFactory =  HibernateUtil.getSessionFactory();

    loggerDAO = new LoggerDAO(sessionFactory);
  }

  @AfterAll
  void tearDown() {
    if (sessionFactory != null) {
      sessionFactory.close();
    }
  }

  @Test
  void testCreateLogger() {
    User user = new User("TestUser", "password");  // Beispieluser für das Log
    Logger logger = new Logger("INFO", "Test log message");
    logger.setUser(user);

    loggerDAO.createLogger(logger);

    Logger retrievedLogger = loggerDAO.getLoggerById(logger.getId());
    assertNotNull(retrievedLogger);
    assertEquals("Test log message", retrievedLogger.getMessage());
    assertEquals("INFO", retrievedLogger.getLevel());
  }

  @Test
  void testGetLoggerById() {
    Logger logger = new Logger("INFO", "Another log message");
    loggerDAO.createLogger(logger);

    Logger retrievedLogger = loggerDAO.getLoggerById(logger.getId());
    assertNotNull(retrievedLogger);
    assertEquals("Another log message", retrievedLogger.getMessage());
  }

  @Test
  void testGetAllLoggers() {
    Logger logger1 = new Logger("ERROR", "Error occurred");
    Logger logger2 = new Logger("DEBUG", "Debugging the app");

    loggerDAO.createLogger(logger1);
    loggerDAO.createLogger(logger2);

    List<Logger> loggers = loggerDAO.getAllLoggers();
    assertNotNull(loggers);
    assertEquals(2, loggers.size());
  }

  @Test
  void testDeleteLogger() {
    Logger logger = new Logger("WARNING", "Test delete log");
    loggerDAO.createLogger(logger);

    Long loggerId = logger.getId();
    loggerDAO.deleteLogger(loggerId);

    Logger deletedLogger = loggerDAO.getLoggerById(loggerId);
    assertNull(deletedLogger);
  }
}
