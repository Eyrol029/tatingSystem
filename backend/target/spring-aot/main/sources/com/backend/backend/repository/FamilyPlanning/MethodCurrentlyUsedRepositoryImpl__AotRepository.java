package com.backend.backend.repository.FamilyPlanning;

import com.backend.backend.model.FamilyPlanning.MethodCurrentlyUsed;
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
 * AOT generated JPA repository implementation for {@link MethodCurrentlyUsedRepository}.
 */
@Generated
public class MethodCurrentlyUsedRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public MethodCurrentlyUsedRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link MethodCurrentlyUsedRepository#findByTypeID(java.lang.Integer)}.
   */
  public List<MethodCurrentlyUsed> findByTypeID(Integer typeID) {
    String queryString = "SELECT m FROM MethodCurrentlyUsed m WHERE m.typeID = :typeID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("typeID", typeID);

    return (List<MethodCurrentlyUsed>) query.getResultList();
  }
}
