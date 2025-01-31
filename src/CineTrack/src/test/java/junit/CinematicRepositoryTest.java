package junit;

import org.com.model.repository.CinematicRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.com.model.domain.Cinematic;
import org.com.model.domain.User;
import org.com.model.repository.AbstractRepository.RepositoryException;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

public class CinematicRepositoryTest {

  @Mock
  private SessionFactory sessionFactory;

  @Mock
  private Session session;

  @Mock
  private Transaction transaction;

  @Mock
  private Query<Cinematic> query;

  @Mock
  private Cinematic mockCinematic;

  private CinematicRepository cinematicRepository;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    cinematicRepository = new CinematicRepository(sessionFactory);
    when(sessionFactory.getCurrentSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);
  }

  @Test
  public void testSaveSingleCinematic_Successful() {
    User user = new User("testUser", "password");

    cinematicRepository.saveCinematic(mockCinematic, user);

    verify(session).persist(mockCinematic);
    verify(mockCinematic).setUser(user);
    verify(transaction).commit();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSaveSingleCinematic_Null() {
    User user = new User("testUser", "password");
    cinematicRepository.saveCinematic(null, user);
  }

  @Test
  public void testSaveMultipleCinematics_Successful() {
    User user = new User("testUser", "password");
    List<Cinematic> cinematics = Arrays.asList(
        mock(Cinematic.class),
        mock(Cinematic.class)
    );

    cinematicRepository.saveCinematics(cinematics, user);

    verify(session, times(2)).persist(any(Cinematic.class));
    cinematics.forEach(c -> verify(c).setUser(user));
    verify(transaction).commit();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSaveMultipleCinematics_EmptyList() {
    User user = new User("testUser", "password");
    cinematicRepository.saveCinematics(null, user);
  }

  @Test
  public void testGetAllCinematicsByUser_CinemaitcsExist() {
    User user = new User("testUser", "password");
    List<Cinematic> expectedCinematics = Arrays.asList(
        mock(Cinematic.class),
        mock(Cinematic.class)
    );

    when(session.createQuery(
        "SELECT DISTINCT c FROM Cinematic c " +
            "LEFT JOIN FETCH c.actors " +
            "LEFT JOIN FETCH c.genres " +
            "WHERE c.user = :user", Cinematic.class)).thenReturn(query);
    when(query.setParameter("user", user)).thenReturn(query);
    when(query.list()).thenReturn(expectedCinematics);

    List<Cinematic> retrievedCinematics = cinematicRepository.getAllCinematicsByUser(user);

    assertNotNull(retrievedCinematics);
    assertEquals(2, retrievedCinematics.size());
    verify(transaction).commit();
  }

  @Test
  public void testDeleteAllCinematicsByUser_Successful() {
    User user = new User("testUser", "password");

    when(session.createMutationQuery("DELETE FROM Cinematic c WHERE c.user = :user")).thenReturn(
        query);
    when(query.setParameter("user", user)).thenReturn(query);
    when(query.executeUpdate()).thenReturn(1);

    cinematicRepository.deleteAllCinematicsByUser(user);

    verify(query).executeUpdate();
    verify(transaction).commit();
  }
}