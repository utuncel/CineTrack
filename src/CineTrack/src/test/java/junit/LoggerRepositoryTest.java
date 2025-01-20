package junit;

import org.com.models.logger.Logger;
import org.com.models.user.User;
import org.com.repository.HibernateUtil;
import org.com.repository.LoggerRepository;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoggerRepositoryTest {
  private SessionFactory sessionFactory;
  private LoggerRepository loggerRepository;

  @BeforeAll
  void setUp() {
    sessionFactory =  HibernateUtil.getSessionFactory();

    loggerRepository = new LoggerRepository(sessionFactory);
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

    loggerRepository.createLogger(logger);

    Logger retrievedLogger = loggerRepository.getLoggerById(logger.getId());
    assertNotNull(retrievedLogger);
    assertEquals("Test log message", retrievedLogger.getMessage());
    assertEquals("INFO", retrievedLogger.getLevel());
  }

  @Test
  void testGetLoggerById() {
    Logger logger = new Logger("INFO", "Another log message");
    loggerRepository.createLogger(logger);

    Logger retrievedLogger = loggerRepository.getLoggerById(logger.getId());
    assertNotNull(retrievedLogger);
    assertEquals("Another log message", retrievedLogger.getMessage());
  }

  @Test
  void testGetAllLoggers() {
    Logger logger1 = new Logger("ERROR", "Error occurred");
    Logger logger2 = new Logger("DEBUG", "Debugging the app");

    loggerRepository.createLogger(logger1);
    loggerRepository.createLogger(logger2);

    List<Logger> loggers = loggerRepository.getAllLoggers();
    assertNotNull(loggers);
    assertEquals(2, loggers.size());
  }

  @Test
  void testDeleteLogger() {
    Logger logger = new Logger("WARNING", "Test delete log");
    loggerRepository.createLogger(logger);

    Long loggerId = logger.getId();
    loggerRepository.deleteLogger(loggerId);

    Logger deletedLogger = loggerRepository.getLoggerById(loggerId);
    assertNull(deletedLogger);
  }
}
