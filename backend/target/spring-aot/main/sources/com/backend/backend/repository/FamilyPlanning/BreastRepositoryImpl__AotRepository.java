package com.backend.backend.repository.FamilyPlanning;

import com.backend.backend.model.FamilyPlanning.Breast;
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
 * AOT generated JPA repository implementation for {@link BreastRepository}.
 */
@Generated
public class BreastRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public BreastRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link BreastRepository#findBypExamID(java.lang.Integer)}.
   */
  public List<Breast> findBypExamID(Integer pExamID) {
    String queryString = "SELECT b FROM Breast b WHERE b.pExamID = :pExamID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("pExamID", pExamID);

    return (List<Breast>) query.getResultList();
  }
}
