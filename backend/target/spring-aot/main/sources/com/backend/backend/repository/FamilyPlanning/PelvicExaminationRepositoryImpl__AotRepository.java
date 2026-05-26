package com.backend.backend.repository.FamilyPlanning;

import com.backend.backend.model.FamilyPlanning.PelvicExamination;
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
 * AOT generated JPA repository implementation for {@link PelvicExaminationRepository}.
 */
@Generated
public class PelvicExaminationRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public PelvicExaminationRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link PelvicExaminationRepository#findBypExamID(java.lang.Integer)}.
   */
  public List<PelvicExamination> findBypExamID(Integer pExamID) {
    String queryString = "SELECT p FROM PelvicExamination p WHERE p.pExamID = :pExamID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("pExamID", pExamID);

    return (List<PelvicExamination>) query.getResultList();
  }
}
