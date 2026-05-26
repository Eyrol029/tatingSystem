package com.backend.backend.repository.FamilyPlanning;

import com.backend.backend.model.FamilyPlanning.FpAssessmentRecord;
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
 * AOT generated JPA repository implementation for {@link FpAssessmentRecordRepository}.
 */
@Generated
public class FpAssessmentRecordRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public FpAssessmentRecordRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link FpAssessmentRecordRepository#findByClientID(java.lang.Integer)}.
   */
  public List<FpAssessmentRecord> findByClientID(Integer clientID) {
    String queryString = "SELECT f FROM FpAssessmentRecord f WHERE f.clientID = :clientID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("clientID", clientID);

    return (List<FpAssessmentRecord>) query.getResultList();
  }
}
