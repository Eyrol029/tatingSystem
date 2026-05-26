package com.backend.backend.service;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link EmployeeServiceImpl}.
 */
@Generated
public class EmployeeServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'employeeServiceImpl'.
   */
  public static BeanDefinition getEmployeeServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(EmployeeServiceImpl.class);
    InstanceSupplier<EmployeeServiceImpl> instanceSupplier = InstanceSupplier.using(EmployeeServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(EmployeeServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
