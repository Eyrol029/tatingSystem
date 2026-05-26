package com.backend.backend.repository.FamilyPlanning;

import com.backend.backend.model.FamilyPlanning.TypeOfClient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.String;
import java.util.List;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link TypeOfClientRepository}.
 */
@Generated
public class TypeOfClientRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public TypeOfClientRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link TypeOfClientRepository#findByClientID(java.lang.Integer)}.
   */
  public List<TypeOfClient> findByClientID(Integer clientID) {
    String queryString = "SELECT t FROM TypeOfClient t WHERE t.clientID = :clientID";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("clientID", clientID);

    return (List<TypeOfClient>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link TypeOfClientRepository#findByIsCurrentUser(java.lang.Boolean)}.
   */
  public List<TypeOfClient> findByIsCurrentUser(Boolean isCurrentUser) {
    String queryString = "SELECT t FROM TypeOfClient t WHERE t.isCurrentUser = :isCurrentUser";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("isCurrentUser", isCurrentUser);

    return (List<TypeOfClient>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link TypeOfClientRepository#findByIsNewAcceptor(java.lang.Boolean)}.
   */
  public List<TypeOfClient> findByIsNewAcceptor(Boolean isNewAcceptor) {
    String queryString = "SELECT t FROM TypeOfClient t WHERE t.isNewAcceptor = :isNewAcceptor";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("isNewAcceptor", isNewAcceptor);

    return (List<TypeOfClient>) query.getResultList();
  }
}
