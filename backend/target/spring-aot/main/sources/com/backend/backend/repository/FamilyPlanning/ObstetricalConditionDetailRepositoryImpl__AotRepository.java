package com.backend.backend.repository.FamilyPlanning;

import com.backend.backend.model.FamilyPlanning.ObstetricalConditionDetail;
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
 * AOT generated JPA repository implementation for {@link ObstetricalConditionDetailRepository}.
 */
@Generated
public class ObstetricalConditionDetailRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public ObstetricalConditionDetailRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link ObstetricalConditionDetailRepository#findByObstetricalHistoryID(java.lang.Integer)}.
   */
  public List<ObstetricalConditionDetail> findByObstetricalHistoryID(Integer obstetricalHistoryID) {
    String queryString = "SELECT o FROM ObstetricalConditionDetail o WHERE o.obstetricalHistoryID = :obstetricalHistoryID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("obstetricalHistoryID", obstetricalHistoryID);

    return (List<ObstetricalConditionDetail>) query.getResultList();
  }
}
