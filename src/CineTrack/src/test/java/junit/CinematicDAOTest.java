package junit;

import org.com.models.Cinematic;
import org.com.models.enums.State;
import org.com.models.enums.Type;
import org.com.repository.CinematicDAO;
import org.com.repository.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CinematicDAOTest {
  private SessionFactory sessionFactory;
  private CinematicDAO cinematicDAO;

  @BeforeAll
  void setUp() {
    sessionFactory = HibernateUtil.getSessionFactory();
    cinematicDAO = new CinematicDAO(sessionFactory);
  }

  @AfterAll
  void tearDown() {
    if (sessionFactory != null) {
      sessionFactory.close();
    }
  }

  @Test
  void testCreateCinematic() {
    Cinematic cinematic = new Cinematic("Test Movie", "Test Description", 120, 8.5, 5000, "Director Name", State.FINISHED, Type.MOVIE);
    cinematicDAO.createCinematic(cinematic);

    Cinematic retrievedCinematic = cinematicDAO.getCinematicById(cinematic.getId());
    assertNotNull(retrievedCinematic);
    assertEquals("Test Movie", retrievedCinematic.getTitle());
    assertEquals("Test Description", retrievedCinematic.getDescription());
  }

  @Test
  void testGetCinematicById() {
    Cinematic cinematic = new Cinematic("Test Movie 2", "Another Description", 130, 7.9, 8000, "Another Director", State.WATCHING, Type.MOVIE);
    cinematicDAO.createCinematic(cinematic);

    Cinematic retrievedCinematic = cinematicDAO.getCinematicById(cinematic.getId());
    assertNotNull(retrievedCinematic);
    assertEquals("Test Movie 2", retrievedCinematic.getTitle());
  }

  @Test
  void testUpdateCinematic() {
    Cinematic cinematic = new Cinematic("Movie to Update", "Old Description", 90, 6.0, 2000, "Old Director", State.WATCHING, Type.MOVIE);
    cinematicDAO.createCinematic(cinematic);

    cinematic.setDescription("Updated Description");
    cinematicDAO.updateCinematic(cinematic);

    Cinematic updatedCinematic = cinematicDAO.getCinematicById(cinematic.getId());
    assertNotNull(updatedCinematic);
    assertEquals("Updated Description", updatedCinematic.getDescription());
  }

  @Test
  void testDeleteCinematic() {
    Cinematic cinematic = new Cinematic("Movie to Delete", "Description to Delete", 150, 9.0, 10000, "Director to Delete", State.FINISHED, Type.MOVIE);
    cinematicDAO.createCinematic(cinematic);

    cinematicDAO.deleteCinematic(cinematic.getId());
    Cinematic deletedCinematic = cinematicDAO.getCinematicById(cinematic.getId());
    assertNull(deletedCinematic);
  }
}
