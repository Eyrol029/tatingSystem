package com.backend.backend.repository;

import com.backend.backend.model.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.lang.String;
import java.util.List;
import org.springframework.aot.generate.Generated;
import org.springframework.data.jpa.repository.aot.AotRepositoryFragmentSupport;
import org.springframework.data.jpa.repository.query.QueryEnhancerSelector;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;

/**
 * AOT generated JPA repository implementation for {@link EmployeeRepository}.
 */
@Generated
public class EmployeeRepositoryImpl__AotRepository extends AotRepositoryFragmentSupport {
  private final RepositoryFactoryBeanSupport.FragmentCreationContext context;

  private final EntityManager entityManager;

  public EmployeeRepositoryImpl__AotRepository(EntityManager entityManager,
      RepositoryFactoryBeanSupport.FragmentCreationContext context) {
    super(QueryEnhancerSelector.DEFAULT_SELECTOR, context);
    this.entityManager = entityManager;
    this.context = context;
  }

  /**
   * AOT generated implementation of {@link EmployeeRepository#findByDepartment(java.lang.String)}.
   */
  public List<Employee> findByDepartment(String department) {
    String queryString = "SELECT e FROM Employee e WHERE e.department = :department";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("department", department);

    return (List<Employee>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link EmployeeRepository#findByPosition(java.lang.String)}.
   */
  public List<Employee> findByPosition(String position) {
    String queryString = "SELECT e FROM Employee e WHERE e.position = :position";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("position", position);

    return (List<Employee>) query.getResultList();
  }

  /**
   * AOT generated implementation of {@link EmployeeRepository#findByStatus(java.lang.String)}.
   */
  public List<Employee> findByStatus(String status) {
    String queryString = "SELECT e FROM Employee e WHERE e.status = :status";
    Query query = this.entityManager.createQuery(queryString);
    query.setParameter("status", status);

    return (List<Employee>) query.getResultList();
  }
}
