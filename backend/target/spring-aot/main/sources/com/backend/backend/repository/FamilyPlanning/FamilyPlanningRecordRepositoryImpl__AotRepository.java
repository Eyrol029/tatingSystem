package com.backend.backend.repository.FamilyPlanning;

import com.backend.backend.model.FamilyPlanning.FamilyPlanningRecord;
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
 * AOT generated JPA repository implementation for {@link FamilyPlanningRecordRepository}.
 */
@Generated
public class FamilyPlanningRecordRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public FamilyPlanningRecordRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link FamilyPlanningRecordRepository#findByClientID(java.lang.Integer)}.
   */
  public List<FamilyPlanningRecord> findByClientID(Integer clientID) {
    String queryString = "SELECT f FROM FamilyPlanningRecord f WHERE f.clientID = :clientID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("clientID", clientID);

    return (List<FamilyPlanningRecord>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link FamilyPlanningRecordRepository#findByServiceID(java.lang.Integer)}.
   */
  public List<FamilyPlanningRecord> findByServiceID(Integer serviceID) {
    String queryString = "SELECT f FROM FamilyPlanningRecord f WHERE f.serviceID = :serviceID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("serviceID", serviceID);

    return (List<FamilyPlanningRecord>) query.getResultList();
  }
}
