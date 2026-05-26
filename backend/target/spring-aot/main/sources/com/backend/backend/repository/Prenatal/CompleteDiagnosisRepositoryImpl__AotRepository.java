package com.backend.backend.repository.Prenatal;

import com.backend.backend.model.Prenatal.CompleteDiagnosis;
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
 * AOT generated JPA repository implementation for {@link CompleteDiagnosisRepository}.
 */
@Generated
public class CompleteDiagnosisRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public CompleteDiagnosisRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link CompleteDiagnosisRepository#findByClinicalHistoryID(java.lang.Integer)}.
   */
  public List<CompleteDiagnosis> findByClinicalHistoryID(Integer clinicalHistoryID) {
    String queryString = "SELECT c FROM CompleteDiagnosis c WHERE c.clinicalHistoryID = :clinicalHistoryID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("clinicalHistoryID", clinicalHistoryID);

    return (List<CompleteDiagnosis>) query.getResultList();
  }
}
