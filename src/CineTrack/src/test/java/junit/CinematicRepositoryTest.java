package junit;

import org.com.model.domain.Cinematic;
import org.com.model.enums.State;
import org.com.model.enums.Type;
import org.com.model.repository.CinematicRepository;
import org.com.model.repository.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CinematicRepositoryTest {
  private SessionFactory sessionFactory;
  private CinematicRepository cinematicRepository;

  @BeforeAll
  void setUp() {
    sessionFactory = HibernateUtil.getSessionFactory();
    cinematicRepository = new CinematicRepository(sessionFactory);
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
    cinematicRepository.createCinematic(cinematic);

    Cinematic retrievedCinematic = cinematicRepository.getCinematicById(cinematic.getId());
    assertNotNull(retrievedCinematic);
    assertEquals("Test Movie", retrievedCinematic.getTitle());
    assertEquals("Test Description", retrievedCinematic.getDescription());
  }

  @Test
  void testGetCinematicById() {
    Cinematic cinematic = new Cinematic("Test Movie 2", "Another Description", 130, 7.9, 8000, "Another Director", State.WATCHING, Type.MOVIE);
    cinematicRepository.createCinematic(cinematic);

    Cinematic retrievedCinematic = cinematicRepository.getCinematicById(cinematic.getId());
    assertNotNull(retrievedCinematic);
    assertEquals("Test Movie 2", retrievedCinematic.getTitle());
  }

  @Test
  void testUpdateCinematic() {
    Cinematic cinematic = new Cinematic("Movie to Update", "Old Description", 90, 6.0, 2000, "Old Director", State.WATCHING, Type.MOVIE);
    cinematicRepository.createCinematic(cinematic);

    cinematic.setDescription("Updated Description");
    cinematicRepository.updateCinematic(cinematic);

    Cinematic updatedCinematic = cinematicRepository.getCinematicById(cinematic.getId());
    assertNotNull(updatedCinematic);
    assertEquals("Updated Description", updatedCinematic.getDescription());
  }

  @Test
  void testDeleteCinematic() {
    Cinematic cinematic = new Cinematic("Movie to Delete", "Description to Delete", 150, 9.0, 10000, "Director to Delete", State.FINISHED, Type.MOVIE);
    cinematicRepository.createCinematic(cinematic);

    cinematicRepository.deleteCinematic(cinematic.getId());
    Cinematic deletedCinematic = cinematicRepository.getCinematicById(cinematic.getId());
    assertNull(deletedCinematic);
  }
}
