package com.backend.backend.repository.Prenatal;

import com.backend.backend.model.Prenatal.AscertainPregnancyLowRisk;
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
 * AOT generated JPA repository implementation for {@link AscertainPregnancyLowRiskRepository}.
 */
@Generated
public class AscertainPregnancyLowRiskRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public AscertainPregnancyLowRiskRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link AscertainPregnancyLowRiskRepository#findByClinicalHistoryID(java.lang.Integer)}.
   */
  public List<AscertainPregnancyLowRisk> findByClinicalHistoryID(Integer clinicalHistoryID) {
    String queryString = "SELECT a FROM AscertainPregnancyLowRisk a WHERE a.clinicalHistoryID = :clinicalHistoryID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("clinicalHistoryID", clinicalHistoryID);

    return (List<AscertainPregnancyLowRisk>) query.getResultList();
  }
}
