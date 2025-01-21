package junit;

import org.com.model.logger.Logger;
import org.com.model.user.User;
import org.com.repository.HibernateUtil;
import org.com.repository.LogRepository;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LogRepositoryTest {
  private SessionFactory sessionFactory;
  private LogRepository logRepository;

  @BeforeAll
  void setUp() {
    sessionFactory =  HibernateUtil.getSessionFactory();

    logRepository = new LogRepository(sessionFactory);
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

    logRepository.createLogger(logger);

    Logger retrievedLogger = logRepository.getLoggerById(logger.getId());
    assertNotNull(retrievedLogger);
    assertEquals("Test log message", retrievedLogger.getMessage());
    assertEquals("INFO", retrievedLogger.getLevel());
  }

  @Test
  void testGetLoggerById() {
    Logger logger = new Logger("INFO", "Another log message");
    logRepository.createLogger(logger);

    Logger retrievedLogger = logRepository.getLoggerById(logger.getId());
    assertNotNull(retrievedLogger);
    assertEquals("Another log message", retrievedLogger.getMessage());
  }

  @Test
  void testGetAllLoggers() {
    Logger logger1 = new Logger("ERROR", "Error occurred");
    Logger logger2 = new Logger("DEBUG", "Debugging the app");

    logRepository.createLogger(logger1);
    logRepository.createLogger(logger2);

    List<Logger> loggers = logRepository.getAllLoggers();
    assertNotNull(loggers);
    assertEquals(2, loggers.size());
  }

  @Test
  void testDeleteLogger() {
    Logger logger = new Logger("WARNING", "Test delete log");
    logRepository.createLogger(logger);

    Long loggerId = logger.getId();
    logRepository.deleteLogger(loggerId);

    Logger deletedLogger = logRepository.getLoggerById(loggerId);
    assertNull(deletedLogger);
  }
}
