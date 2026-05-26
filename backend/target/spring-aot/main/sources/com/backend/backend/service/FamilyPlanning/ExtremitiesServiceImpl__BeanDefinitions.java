package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ExtremitiesServiceImpl}.
 */
@Generated
public class ExtremitiesServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'extremitiesServiceImpl'.
   */
  public static BeanDefinition getExtremitiesServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ExtremitiesServiceImpl.class);
    InstanceSupplier<ExtremitiesServiceImpl> instanceSupplier = InstanceSupplier.using(ExtremitiesServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(ExtremitiesServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
