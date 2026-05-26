package com.backend.backend.repository.FamilyPlanning;

import com.backend.backend.model.FamilyPlanning.PregnancyExclusionChecklist;
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
 * AOT generated JPA repository implementation for {@link PregnancyExclusionChecklistRepository}.
 */
@Generated
public class PregnancyExclusionChecklistRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public PregnancyExclusionChecklistRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link PregnancyExclusionChecklistRepository#findByClientID(java.lang.Integer)}.
   */
  public List<PregnancyExclusionChecklist> findByClientID(Integer clientID) {
    String queryString = "SELECT p FROM PregnancyExclusionChecklist p WHERE p.clientID = :clientID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("clientID", clientID);

    return (List<PregnancyExclusionChecklist>) query.getResultList();
  }
}
