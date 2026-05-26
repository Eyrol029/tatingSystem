package com.backend.backend.repository.Prenatal;

import com.backend.backend.model.Prenatal.ObstetricRiskFactor;
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
 * AOT generated JPA repository implementation for {@link ObstetricRiskFactorRepository}.
 */
@Generated
public class ObstetricRiskFactorRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public ObstetricRiskFactorRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link ObstetricRiskFactorRepository#findByClinicalHistoryID(java.lang.Integer)}.
   */
  public List<ObstetricRiskFactor> findByClinicalHistoryID(Integer clinicalHistoryID) {
    String queryString = "SELECT o FROM ObstetricRiskFactor o WHERE o.clinicalHistoryID = :clinicalHistoryID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("clinicalHistoryID", clinicalHistoryID);

    return (List<ObstetricRiskFactor>) query.getResultList();
  }
}
