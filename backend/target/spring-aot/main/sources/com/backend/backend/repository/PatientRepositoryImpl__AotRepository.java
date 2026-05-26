package com.backend.backend.repository;

import com.backend.backend.model.Patient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.String;
import java.util.List;
import java.util.Optional;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link PatientRepository}.
 */
@Generated
public class PatientRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public PatientRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link PatientRepository#findByBarangay(java.lang.String)}.
   */
  public List<Patient> findByBarangay(String barangay) {
    String queryString = "SELECT p FROM Patient p WHERE p.barangay = :barangay";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("barangay", barangay);

    return (List<Patient>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link PatientRepository#findByEmail(java.lang.String)}.
   */
  public Optional<Patient> findByEmail(String email) {
    String queryString = "SELECT p FROM Patient p WHERE p.email = :email";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("email", email);

    return Optional.ofNullable((Patient) convertOne(query.getSingleResultOrNull(), false, Patient.class));
  }

  /**
   * AOT generated implementation of {@link PatientRepository#findByLName(java.lang.String)}.
   */
  public List<Patient> findByLName(String lName) {
    String queryString = "SELECT p FROM Patient p WHERE p.LName = :lName";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("lName", lName);

    return (List<Patient>) query.getResultList();
  }
}
