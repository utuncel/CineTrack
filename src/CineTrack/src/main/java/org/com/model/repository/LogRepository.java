package org.com.model.repository;

import java.util.List;
import org.com.model.domain.Log;
import org.com.model.domain.User;
import org.hibernate.SessionFactory;

/**
 * Repository for log-related database operations using Hibernate. Handles log persistence and
 * retrieval for users.
 *
 * @author Umut
 * @version 1.0
 */
public class LogRepository extends AbstractRepository {

  /**
   * Constructs LogRepository with a Hibernate SessionFactory.
   *
   * @param sessionFactory Hibernate SessionFactory for database connections
   */
  public LogRepository(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  /**
   * Saves a log entry associated with a user.
   *
   * @param log  Log to be persisted
   * @param user User associated with the log
   */
  public void saveLogger(Log log, User user) {
    executeWithinTransaction(session -> {
      log.setUser(user);
      session.persist(log);
      return null;
    }, "Failed to create log");
  }

  /**
   * Retrieves all logs for a specific user.
   *
   * @param user User whose logs are to be retrieved
   * @return List of logs for the user
   */
  public List<Log> getLogsByUser(User user) {
    return executeWithinTransaction(session ->
            session.createQuery("FROM Log l WHERE l.user = :user", Log.class)
                .setParameter("user", user)
                .list(),
        "Failed to retrieve logs for user");
  }
}