package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.com.model.domain.Log;
import org.com.model.domain.User;
import org.com.model.repository.AbstractRepository.RepositoryException;
import org.com.model.repository.LogRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LogRepositoryTest {

  @Mock
  private SessionFactory sessionFactory;

  @Mock
  private Session session;

  @Mock
  private Transaction transaction;

  @Mock
  private Query<Log> query;

  private LogRepository logRepository;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    logRepository = new LogRepository(sessionFactory);
    when(sessionFactory.getCurrentSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);
  }

  @Test
  public void testSaveLogger_Successful() {
    User user = new User("testUser", "password");
    Log log = new Log("INFO", "Test log message");

    logRepository.saveLogger(log, user);

    verify(session).persist(log);
    assertEquals(user, log.getUser());
    verify(transaction).commit();
  }

  @Test(expected = RepositoryException.class)
  public void testSaveLogger_Failure() {
    User user = new User("testUser", "password");
    Log log = new Log("INFO", "Test log message");

    doThrow(new RuntimeException("Database error")).when(session).persist(log);

    logRepository.saveLogger(log, user);
  }

  @Test
  public void testGetLogsByUser_LogsExist() {
    User user = new User("testUser", "password");
    List<Log> expectedLogs = Arrays.asList(
        new Log("INFO", "Log 1"),
        new Log("ERROR", "Log 2")
    );

    when(session.createQuery("FROM Log l WHERE l.user = :user", Log.class)).thenReturn(query);
    when(query.setParameter("user", user)).thenReturn(query);
    when(query.list()).thenReturn(expectedLogs);

    List<Log> retrievedLogs = logRepository.getLogsByUser(user);

    assertNotNull(retrievedLogs);
    assertEquals(2, retrievedLogs.size());
    verify(transaction).commit();
  }

  @Test
  public void testGetLogsByUser_NoLogsFound() {
    User user = new User("testUser", "password");

    when(session.createQuery("FROM Log l WHERE l.user = :user", Log.class)).thenReturn(query);
    when(query.setParameter("user", user)).thenReturn(query);
    when(query.list()).thenReturn(Arrays.asList());

    List<Log> retrievedLogs = logRepository.getLogsByUser(user);

    assertTrue(retrievedLogs.isEmpty());
    verify(transaction).commit();
  }
}