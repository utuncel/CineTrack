package org.com.repository;

import java.util.ArrayList;
import java.util.List;
import org.com.models.user.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class UserDAO {
  private SessionFactory sessionFactory;

  public UserDAO(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public void createUser(User user) {
    Session session = null;
    Transaction transaction = null;
    try {
      session = sessionFactory.getCurrentSession();
      transaction = session.beginTransaction();
      session.persist(user);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw new RuntimeException("User creation failed", e);
    }
  }

  public User getUserById(Long id) {
    Session session = null;
    Transaction transaction = null;
    try {
      session = sessionFactory.getCurrentSession();
      transaction = session.beginTransaction();
      User user = session.get(User.class, id);
      transaction.commit();
      return user;
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw new RuntimeException("Get user by ID failed", e);
    }
  }

  public User getUserByName(String username) {
    Session session = null;
    Transaction transaction = null;
    try {
      session = sessionFactory.getCurrentSession();
      transaction = session.beginTransaction();
      User user = session.createQuery("FROM User u WHERE u.name = :name", User.class)
          .setParameter("name", username)
          .uniqueResult();
      transaction.commit();
      return user;
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw new RuntimeException("Get user by name failed", e);
    }
  }

  public User authenticate(String username, String password) {
    Session session = null;
    Transaction transaction = null;
    try {
      session = sessionFactory.getCurrentSession();
      transaction = session.beginTransaction();
      Query<User> query = session.createQuery("FROM User u WHERE u.name = :name", User.class);
      query.setParameter("name", username);
      User user = query.uniqueResult();

      if (user != null && user.getPassword().equals(password)) {
        transaction.commit();
        return user;
      }

      transaction.commit();
      return null;
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw new RuntimeException("Authentication failed", e);
    }
  }

  public List<User> getAllUsers() {
    Session session = null;
    Transaction transaction = null;
    try {
      session = sessionFactory.getCurrentSession();
      transaction = session.beginTransaction();
      List<User> users = new ArrayList<>(session.createQuery("FROM User", User.class).list());
      transaction.commit();
      return users;
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw new RuntimeException("Get all users failed", e);
    }
  }

  public void updateUser(User user) {
    Session session = null;
    Transaction transaction = null;
    try {
      session = sessionFactory.getCurrentSession();
      transaction = session.beginTransaction();
      session.merge(user);
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw new RuntimeException("User update failed", e);
    }
  }

  public void deleteUser(Long id) {
    Session session = null;
    Transaction transaction = null;
    try {
      session = sessionFactory.getCurrentSession();
      transaction = session.beginTransaction();
      User user = session.get(User.class, id);
      if (user != null) {
        session.remove(user);
      }
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw new RuntimeException("User deletion failed", e);
    }
  }
}