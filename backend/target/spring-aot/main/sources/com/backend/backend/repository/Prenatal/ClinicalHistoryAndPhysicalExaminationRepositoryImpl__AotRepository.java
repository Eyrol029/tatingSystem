package com.backend.backend.repository.Prenatal;

import com.backend.backend.model.Prenatal.ClinicalHistoryAndPhysicalExamination;
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
 * AOT generated JPA repository implementation for {@link ClinicalHistoryAndPhysicalExaminationRepository}.
 */
@Generated
public class ClinicalHistoryAndPhysicalExaminationRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public ClinicalHistoryAndPhysicalExaminationRepositoryImpl__AotRepository(
      EntityManager entityManager, RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link ClinicalHistoryAndPhysicalExaminationRepository#findByPrenatalrecordID(java.lang.Integer)}.
   */
  public List<ClinicalHistoryAndPhysicalExamination> findByPrenatalrecordID(
      Integer prenatalrecordID) {
    String queryString = "SELECT c FROM ClinicalHistoryAndPhysicalExamination c WHERE c.prenatalrecordID = :prenatalrecordID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("prenatalrecordID", prenatalrecordID);

    return (List<ClinicalHistoryAndPhysicalExamination>) query.getResultList();
  }
}
