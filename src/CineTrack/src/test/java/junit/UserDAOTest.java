package junit;

import org.com.models.user.User;
import org.com.repository.UserDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDAOTest {
  private SessionFactory mockSessionFactory;
  private Session mockSession;
  private Transaction mockTransaction;
  private UserDAO userDAO;

  @BeforeEach
  void setUp() {
    mockSessionFactory = mock(SessionFactory.class);
    mockSession = mock(Session.class);
    mockTransaction = mock(Transaction.class);

    when(mockSessionFactory.openSession()).thenReturn(mockSession);
    when(mockSession.beginTransaction()).thenReturn(mockTransaction);

    userDAO = new UserDAO(mockSessionFactory);
  }

  @Test
  void testCreateUser() {
    User user = new User("TestUser", "password");

    doNothing().when(mockSession).persist(user);

    userDAO.createUser(user);

    verify(mockSession).persist(user);
    verify(mockTransaction).commit();
  }

  @Test
  void testGetUserById() {
    User user = new User("TestUser2", "password2");

    when(mockSession.get(User.class, user.getId())).thenReturn(user);

    User retrievedUser = userDAO.getUserById(user.getId());
    assertNotNull(retrievedUser);
    assertEquals("TestUser2", retrievedUser.getName());

    verify(mockSession).get(User.class, user.getId());
  }

  @Test
  public void testGetUserByName_Success() {
    User mockUser = new User("testuser", "password");

    Query<User> mockQuery = mock(Query.class);
    when(mockSession.createQuery("FROM User u WHERE u.username = :username", User.class)).thenReturn(mockQuery);
    when(mockQuery.setParameter("username", "testuser")).thenReturn(mockQuery);
    when(mockQuery.uniqueResult()).thenReturn(mockUser);

    User result = userDAO.getUserByName("testuser");

    assertNotNull(result);
    assertEquals("testuser", result.getName());
  }

  @Test
  void testGetAllUsers() {
    Query<User> mockQuery = mock(Query.class);
    when(mockSession.createQuery("FROM User", User.class)).thenReturn(mockQuery);
    when(mockQuery.list()).thenReturn(List.of(new User("John", "password123")));

    List<User> users = userDAO.getAllUsers();

    assertNotNull(users);
    assertEquals(1, users.size());
    assertEquals("John", users.getFirst().getName());

    verify(mockSession).createQuery("FROM User", User.class);
    verify(mockQuery).list();
  }


  @Test
  void testUpdateUser() {
    User user = new User("UserToUpdate", "oldPassword");
    user.setId(1L);

    doNothing().when(mockSession).update(user);

    userDAO.updateUser(user);

    verify(mockSession).update(user);
    verify(mockTransaction).commit();
  }


  @Test
  void testDeleteUser() {
    User user = new User("UserToDelete", "password");
    user.setId(1L);

    when(mockSession.get(User.class, user.getId())).thenReturn(user);

    doNothing().when(mockSession).delete(user);

    userDAO.deleteUser(user.getId());

    verify(mockSession).get(User.class, user.getId());
    verify(mockSession).delete(user);
    verify(mockTransaction).commit();
  }

  @Test
  public void testAuthenticate_Success() {
    // Erstellen eines Mock-Users
    User mockUser = new User("testuser","password123");


    Query<User> mockQuery = mock(Query.class);
    when(mockSession.createQuery("FROM User u WHERE u.username = :username", User.class)).thenReturn(mockQuery);
    when(mockQuery.setParameter("username", "testuser")).thenReturn(mockQuery);
    when(mockQuery.uniqueResult()).thenReturn(mockUser);

    User result = userDAO.authenticate("testuser", "password123");

    assertNotNull(result);
    assertEquals("testuser", result.getName());
  }

  @Test
  public void testAuthenticate_Failure_WrongPassword() {
    User mockUser = new User("testuser","password123");

    Query<User> mockQuery = mock(Query.class);
    when(mockSession.createQuery("FROM User u WHERE u.username = :username", User.class)).thenReturn(mockQuery);
    when(mockQuery.setParameter("username", "testuser")).thenReturn(mockQuery);
    when(mockQuery.uniqueResult()).thenReturn(mockUser);

    User result = userDAO.authenticate("testuser", "wrongpassword");

    assertNull(result);
  }

  @Test
  public void testAuthenticate_Failure_UserNotFound() {
    Query<User> mockQuery = mock(Query.class);
    when(mockSession.createQuery("FROM User u WHERE u.username = :username", User.class)).thenReturn(mockQuery);
    when(mockQuery.setParameter("username", "nonexistentuser")).thenReturn(mockQuery);
    when(mockQuery.uniqueResult()).thenReturn(null);

    User result = userDAO.authenticate("nonexistentuser", "password123");

    assertNull(result);
  }

}
