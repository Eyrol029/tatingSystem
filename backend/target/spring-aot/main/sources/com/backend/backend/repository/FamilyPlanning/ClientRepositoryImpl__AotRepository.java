package com.backend.backend.repository.FamilyPlanning;

import com.backend.backend.model.FamilyPlanning.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.String;
import java.util.List;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link ClientRepository}.
 */
@Generated
public class ClientRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public ClientRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link ClientRepository#findByCivilStatus(java.lang.String)}.
   */
  public List<Client> findByCivilStatus(String civilStatus) {
    String queryString = "SELECT c FROM Client c WHERE c.civilStatus = :civilStatus";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("civilStatus", civilStatus);

    return (List<Client>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link ClientRepository#findByIs4PSMember(java.lang.String)}.
   */
  public List<Client> findByIs4PSMember(String is4PSMember) {
    String queryString = "SELECT c FROM Client c WHERE c.is4PSMember = :is4PSMember";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("is4PSMember", is4PSMember);

    return (List<Client>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link ClientRepository#findByNhts(java.lang.String)}.
   */
  public List<Client> findByNhts(String nhts) {
    String queryString = "SELECT c FROM Client c WHERE c.nhts = :nhts";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("nhts", nhts);

    return (List<Client>) query.getResultList();
  }
}
