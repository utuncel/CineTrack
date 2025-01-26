package org.com.model.repository;

import java.util.List;
import org.com.model.domain.Cinematic;
import org.com.model.domain.User;
import org.hibernate.SessionFactory;

/**
 * Repository for cinematic-related database operations using Hibernate. Handles persistence,
 * retrieval, and deletion of cinematics for users.
 *
 * @author Umut
 * @version 1.0
 */
public class CinematicRepository extends AbstractRepository {

  /**
   * Constructs CinematicRepository with a Hibernate SessionFactory.
   *
   * @param sessionFactory Hibernate SessionFactory for database connections
   */
  public CinematicRepository(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  /**
   * Saves a single cinematic, associating it with a user.
   *
   * @param cinematic Cinematic to be saved
   * @param user      User to associate with the cinematic
   * @throws IllegalArgumentException If cinematic is null
   */
  public void saveCinematic(Cinematic cinematic, User user) {
    if (cinematic == null) {
      throw new IllegalArgumentException("Cinematic cannot be null.");
    }

    executeWithinTransaction(session -> {
      if (cinematic.getUser() == null) {
        cinematic.setUser(user);
      }
      session.persist(cinematic);
      return null;
    }, "Failed to save the cinematic");
  }

  /**
   * Saves multiple cinematics, associating them with a user.
   *
   * @param cinematics List of cinematics to be saved
   * @param user       User to associate with the cinematics
   * @throws IllegalArgumentException If cinematics list is null or empty
   */
  public void saveCinematics(List<Cinematic> cinematics, User user) {
    if (cinematics == null || cinematics.isEmpty()) {
      throw new IllegalArgumentException("The list of cinematics cannot be empty.");
    }

    executeWithinTransaction(session -> {
      for (Cinematic cinematic : cinematics) {
        if (cinematic.getUser() == null) {
          cinematic.setUser(user);
        }
        session.persist(cinematic);
      }
      return null;
    }, "Failed to save the cinematics list");
  }

  /**
   * Retrieves all cinematics for a specific user with related actors and genres.
   *
   * @param user User whose cinematics are to be retrieved
   * @return List of cinematics for the user
   */
  public List<Cinematic> getAllCinematicsByUser(User user) {
    return executeWithinTransaction(session ->
            session.createQuery(
                    "SELECT DISTINCT c FROM Cinematic c " +
                        "LEFT JOIN FETCH c.actors " +
                        "LEFT JOIN FETCH c.genres " +
                        "WHERE c.user = :user", Cinematic.class)
                .setParameter("user", user)
                .list(),
        "Failed to fetch cinematics for the user");
  }

  /**
   * Deletes all cinematics associated with a specific user.
   *
   * @param user User whose cinematics are to be deleted
   */
  public void deleteAllCinematicsByUser(User user) {
    executeWithinTransaction(session -> {
      session.createMutationQuery("DELETE FROM Cinematic c WHERE c.user = :user")
          .setParameter("user", user)
          .executeUpdate();
      return null;
    }, "Failed to delete cinematics for the user");
  }
}