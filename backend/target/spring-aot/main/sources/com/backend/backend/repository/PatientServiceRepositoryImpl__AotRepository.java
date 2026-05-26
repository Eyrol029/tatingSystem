package com.backend.backend.repository;

import com.backend.backend.model.PatientService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.Integer;
import java.lang.String;
import java.util.List;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link PatientServiceRepository}.
 */
@Generated
public class PatientServiceRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public PatientServiceRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link PatientServiceRepository#findByEmployeeName(java.lang.String)}.
   */
  public List<PatientService> findByEmployeeName(String employeeName) {
    String queryString = "SELECT p FROM PatientService p WHERE p.employeeName = :employeeName";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("employeeName", employeeName);

    return (List<PatientService>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link PatientServiceRepository#findByPatientID(java.lang.Integer)}.
   */
  public List<PatientService> findByPatientID(Integer patientID) {
    String queryString = "SELECT p FROM PatientService p WHERE p.patientID = :patientID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("patientID", patientID);

    return (List<PatientService>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link PatientServiceRepository#findByServiceName(java.lang.String)}.
   */
  public List<PatientService> findByServiceName(String serviceName) {
    String queryString = "SELECT p FROM PatientService p WHERE p.serviceName = :serviceName";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("serviceName", serviceName);

    return (List<PatientService>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link PatientServiceRepository#findByWardName(java.lang.String)}.
   */
  public List<PatientService> findByWardName(String wardName) {
    String queryString = "SELECT p FROM PatientService p WHERE p.wardName = :wardName";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("wardName", wardName);

    return (List<PatientService>) query.getResultList();
  }
}
