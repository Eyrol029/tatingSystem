package com.backend.backend.repository.Inventory;

import com.backend.backend.model.Inventory.ClinicalToolInventory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.String;
import java.util.List;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link ClinicalToolInventoryRepository}.
 */
@Generated
public class ClinicalToolInventoryRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public ClinicalToolInventoryRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link ClinicalToolInventoryRepository#findByToolName(java.lang.String)}.
   */
  public List<ClinicalToolInventory> findByToolName(String toolName) {
    String queryString = "SELECT c FROM ClinicalToolInventory c WHERE c.toolName = :toolName";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("toolName", toolName);

    return (List<ClinicalToolInventory>) query.getResultList();
  }
}
