package com.backend.backend.repository.FamilyPlanning;

import com.backend.backend.model.FamilyPlanning.Spouse;
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
 * AOT generated JPA repository implementation for {@link SpouseRepository}.
 */
@Generated
public class SpouseRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public SpouseRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link SpouseRepository#findByClientID(java.lang.Integer)}.
   */
  public List<Spouse> findByClientID(Integer clientID) {
    String queryString = "SELECT s FROM Spouse s WHERE s.clientID = :clientID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("clientID", clientID);

    return (List<Spouse>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link SpouseRepository#findByLName(java.lang.String)}.
   */
  public List<Spouse> findByLName(String lName) {
    String queryString = "SELECT s FROM Spouse s WHERE s.LName = :lName";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("lName", lName);

    return (List<Spouse>) query.getResultList();
  }
}
