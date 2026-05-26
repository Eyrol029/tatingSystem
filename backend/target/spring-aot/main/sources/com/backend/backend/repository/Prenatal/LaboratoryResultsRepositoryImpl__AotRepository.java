package com.backend.backend.repository.Prenatal;

import com.backend.backend.model.Prenatal.LaboratoryResults;
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
 * AOT generated JPA repository implementation for {@link LaboratoryResultsRepository}.
 */
@Generated
public class LaboratoryResultsRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public LaboratoryResultsRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link LaboratoryResultsRepository#findByPrenatalRecordID(java.lang.Integer)}.
   */
  public List<LaboratoryResults> findByPrenatalRecordID(Integer prenatalRecordID) {
    String queryString = "SELECT l FROM LaboratoryResults l WHERE l.prenatalRecordID = :prenatalRecordID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("prenatalRecordID", prenatalRecordID);

    return (List<LaboratoryResults>) query.getResultList();
  }
}
