package com.backend.backend.repository.Prenatal;

import com.backend.backend.model.Prenatal.VitalSigns;
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
 * AOT generated JPA repository implementation for {@link VitalSignsRepository}.
 */
@Generated
public class VitalSignsRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public VitalSignsRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link VitalSignsRepository#findByConsultationRecordID(java.lang.Integer)}.
   */
  public List<VitalSigns> findByConsultationRecordID(Integer consultationRecordID) {
    String queryString = "SELECT v FROM VitalSigns v WHERE v.consultationRecordID = :consultationRecordID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("consultationRecordID", consultationRecordID);

    return (List<VitalSigns>) query.getResultList();
  }
}
