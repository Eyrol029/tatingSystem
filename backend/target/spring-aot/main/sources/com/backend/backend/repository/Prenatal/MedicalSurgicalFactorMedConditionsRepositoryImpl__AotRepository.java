package com.backend.backend.repository.Prenatal;

import com.backend.backend.model.Prenatal.MedicalSurgicalFactorMedConditions;
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
 * AOT generated JPA repository implementation for {@link MedicalSurgicalFactorMedConditionsRepository}.
 */
@Generated
public class MedicalSurgicalFactorMedConditionsRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public MedicalSurgicalFactorMedConditionsRepositoryImpl__AotRepository(
      EntityManager entityManager, RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link MedicalSurgicalFactorMedConditionsRepository#findByMedsurgID(java.lang.Integer)}.
   */
  public List<MedicalSurgicalFactorMedConditions> findByMedsurgID(Integer medsurgID) {
    String queryString = "SELECT m FROM MedicalSurgicalFactorMedConditions m WHERE m.medsurgID = :medsurgID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("medsurgID", medsurgID);

    return (List<MedicalSurgicalFactorMedConditions>) query.getResultList();
  }
}
