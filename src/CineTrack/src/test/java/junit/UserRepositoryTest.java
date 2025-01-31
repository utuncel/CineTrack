package junit;

import org.com.model.repository.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.com.model.domain.User;
import org.com.model.repository.AbstractRepository.RepositoryException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserRepositoryTest {

  @Mock
  private SessionFactory sessionFactory;

  @Mock
  private Session session;

  @Mock
  private Transaction transaction;

  @Mock
  private Query<User> query;

  private UserRepository userRepository;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    userRepository = new UserRepository(sessionFactory);
    when(sessionFactory.getCurrentSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);
  }

  @Test
  public void testSaveUser_Successful() {
    User user = new User("testUser", "password");
    userRepository.saveUser(user);

    verify(session).persist(user);
    verify(transaction).commit();
  }

  @Test(expected = RepositoryException.class)
  public void testSaveUser_Failure() {
    User user = new User("testUser", "password");
    doThrow(new RuntimeException("Database error")).when(session).persist(user);

    userRepository.saveUser(user);
  }

  @Test
  public void testGetUserByName_UserExists() {
    String username = "existingUser";
    User expectedUser = new User(username, "password");

    when(session.createQuery("FROM User u WHERE u.name = :name", User.class)).thenReturn(query);
    when(query.setParameter("name", username)).thenReturn(query);
    when(query.uniqueResult()).thenReturn(expectedUser);

    User retrievedUser = userRepository.getUserByName(username);

    assertNotNull(retrievedUser);
    assertEquals(username, retrievedUser.getName());
    verify(transaction).commit();
  }

  @Test
  public void testAuthenticateUser_Successful() {
    String username = "validUser";
    String password = "correctPassword";
    User expectedUser = new User(username, password);

    when(session.createQuery("FROM User u WHERE u.name = :name", User.class)).thenReturn(query);
    when(query.setParameter("name", username)).thenReturn(query);
    when(query.uniqueResult()).thenReturn(expectedUser);

    User authenticatedUser = userRepository.authenticateUser(username, password);

    assertNotNull(authenticatedUser);
    assertEquals(username, authenticatedUser.getName());
    verify(transaction).commit();
  }

  @Test
  public void testAuthenticateUser_WrongPassword() {
    String username = "validUser";
    String correctPassword = "correctPassword";
    String wrongPassword = "wrongPassword";
    User expectedUser = new User(username, correctPassword);

    when(session.createQuery("FROM User u WHERE u.name = :name", User.class)).thenReturn(query);
    when(query.setParameter("name", username)).thenReturn(query);
    when(query.uniqueResult()).thenReturn(expectedUser);

    User authenticatedUser = userRepository.authenticateUser(username, wrongPassword);

    assertNull(authenticatedUser);
    verify(transaction).commit();
  }
}