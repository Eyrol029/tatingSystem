package com.backend.backend.repository;

import com.backend.backend.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.Integer;
import java.lang.String;
import java.util.List;
import java.util.Optional;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link UserRepository}.
 */
@Generated
public class UserRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public UserRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link UserRepository#findByEmail(java.lang.String)}.
   */
  public Optional<User> findByEmail(String email) {
    String queryString = "SELECT u FROM User u WHERE u.email = :email";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("email", email);

    return Optional.ofNullable((User) convertOne(query.getSingleResultOrNull(), false, User.class));
  }

  /**
   * AOT generated implementation of {@link UserRepository#findByEmailAndRole(java.lang.String,java.lang.String)}.
   */
  public Optional<User> findByEmailAndRole(String email, String role) {
    String queryString = "SELECT u FROM User u WHERE u.email = :email AND u.role = :role";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("email", email);
    query.setParameter("role", role);

    return Optional.ofNullable((User) convertOne(query.getSingleResultOrNull(), false, User.class));
  }

  /**
   * AOT generated implementation of {@link UserRepository#findByEmployeeID(java.lang.Integer)}.
   */
  public List<User> findByEmployeeID(Integer employeeID) {
    String queryString = "SELECT u FROM User u WHERE u.employeeID = :employeeID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("employeeID", employeeID);

    return (List<User>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link UserRepository#findByPatientID(java.lang.Integer)}.
   */
  public List<User> findByPatientID(Integer patientID) {
    String queryString = "SELECT u FROM User u WHERE u.patientID = :patientID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("patientID", patientID);

    return (List<User>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link UserRepository#findByRole(java.lang.String)}.
   */
  public List<User> findByRole(String role) {
    String queryString = "SELECT u FROM User u WHERE u.role = :role";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("role", role);

    return (List<User>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link UserRepository#findByStatus(java.lang.String)}.
   */
  public List<User> findByStatus(String status) {
    String queryString = "SELECT u FROM User u WHERE u.status = :status";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("status", status);

    return (List<User>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link UserRepository#findByUsername(java.lang.String)}.
   */
  public Optional<User> findByUsername(String username) {
    String queryString = "SELECT u FROM User u WHERE u.username = :username";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("username", username);

    return Optional.ofNullable((User) convertOne(query.getSingleResultOrNull(), false, User.class));
  }
}
