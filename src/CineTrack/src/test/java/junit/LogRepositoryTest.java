package junit;

import org.com.model.domain.Log;
import org.com.model.domain.User;
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
    Log log = new Log("INFO", "Test log message");
    log.setUser(user);

    logRepository.createLogger(log);

    Log retrievedLog = logRepository.getLoggerById(log.getId());
    assertNotNull(retrievedLog);
    assertEquals("Test log message", retrievedLog.getMessage());
    assertEquals("INFO", retrievedLog.getLevel());
  }

  @Test
  void testGetLoggerById() {
    Log log = new Log("INFO", "Another log message");
    logRepository.createLogger(log);

    Log retrievedLog = logRepository.getLoggerById(log.getId());
    assertNotNull(retrievedLog);
    assertEquals("Another log message", retrievedLog.getMessage());
  }

  @Test
  void testGetAllLoggers() {
    Log log1 = new Log("ERROR", "Error occurred");
    Log log2 = new Log("DEBUG", "Debugging the app");

    logRepository.createLogger(log1);
    logRepository.createLogger(log2);

    List<Log> logs = logRepository.getAllLoggers();
    assertNotNull(logs);
    assertEquals(2, logs.size());
  }

  @Test
  void testDeleteLogger() {
    Log log = new Log("WARNING", "Test delete log");
    logRepository.createLogger(log);

    Long loggerId = log.getId();
    logRepository.deleteLogger(loggerId);

    Log deletedLog = logRepository.getLoggerById(loggerId);
    assertNull(deletedLog);
  }
}
