package com.backend.backend.repository.FamilyPlanning;

import com.backend.backend.model.FamilyPlanning.ObstetricalHistory;
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
 * AOT generated JPA repository implementation for {@link ObstetricalHistoryRepository}.
 */
@Generated
public class ObstetricalHistoryRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public ObstetricalHistoryRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link ObstetricalHistoryRepository#findByClientID(java.lang.Integer)}.
   */
  public List<ObstetricalHistory> findByClientID(Integer clientID) {
    String queryString = "SELECT o FROM ObstetricalHistory o WHERE o.clientID = :clientID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("clientID", clientID);

    return (List<ObstetricalHistory>) query.getResultList();
  }
}
