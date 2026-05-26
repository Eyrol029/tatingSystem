package com.backend.backend.repository.FamilyPlanning;

import com.backend.backend.model.FamilyPlanning.STI_RiskDetail;
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
 * AOT generated JPA repository implementation for {@link STI_RiskDetailRepository}.
 */
@Generated
public class STI_RiskDetailRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public STI_RiskDetailRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link STI_RiskDetailRepository#findByStiConditionID(java.lang.Integer)}.
   */
  public List<STI_RiskDetail> findByStiConditionID(Integer stiConditionID) {
    String queryString = "SELECT s FROM STI_RiskDetail s WHERE s.stiConditionID = :stiConditionID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("stiConditionID", stiConditionID);

    return (List<STI_RiskDetail>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link STI_RiskDetailRepository#findByStiRiskID(java.lang.Integer)}.
   */
  public List<STI_RiskDetail> findByStiRiskID(Integer stiRiskID) {
    String queryString = "SELECT s FROM STI_RiskDetail s WHERE s.stiRiskID = :stiRiskID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("stiRiskID", stiRiskID);

    return (List<STI_RiskDetail>) query.getResultList();
  }
}
