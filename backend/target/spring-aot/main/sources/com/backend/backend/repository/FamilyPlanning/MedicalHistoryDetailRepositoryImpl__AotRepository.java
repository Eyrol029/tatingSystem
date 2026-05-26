package com.backend.backend.repository.FamilyPlanning;

import com.backend.backend.model.FamilyPlanning.MedicalHistoryDetail;
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
 * AOT generated JPA repository implementation for {@link MedicalHistoryDetailRepository}.
 */
@Generated
public class MedicalHistoryDetailRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public MedicalHistoryDetailRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link MedicalHistoryDetailRepository#findByMedicalHistoryid(java.lang.Integer)}.
   */
  public List<MedicalHistoryDetail> findByMedicalHistoryid(Integer medicalHistoryid) {
    String queryString = "SELECT m FROM MedicalHistoryDetail m WHERE m.medicalHistoryid = :medicalHistoryid";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("medicalHistoryid", medicalHistoryid);

    return (List<MedicalHistoryDetail>) query.getResultList();
  }
}
