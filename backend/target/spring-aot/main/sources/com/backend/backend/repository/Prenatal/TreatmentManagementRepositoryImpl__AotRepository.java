package com.backend.backend.repository.Prenatal;

import com.backend.backend.model.Prenatal.TreatmentManagement;
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
 * AOT generated JPA repository implementation for {@link TreatmentManagementRepository}.
 */
@Generated
public class TreatmentManagementRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public TreatmentManagementRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link TreatmentManagementRepository#findByPrenatalRecordID(java.lang.Integer)}.
   */
  public List<TreatmentManagement> findByPrenatalRecordID(Integer prenatalRecordID) {
    String queryString = "SELECT t FROM TreatmentManagement t WHERE t.prenatalRecordID = :prenatalRecordID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("prenatalRecordID", prenatalRecordID);

    return (List<TreatmentManagement>) query.getResultList();
  }
}
