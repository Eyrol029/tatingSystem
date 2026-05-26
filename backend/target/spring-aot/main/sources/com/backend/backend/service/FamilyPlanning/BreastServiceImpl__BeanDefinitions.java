package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link BreastServiceImpl}.
 */
@Generated
public class BreastServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'breastServiceImpl'.
   */
  public static BeanDefinition getBreastServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(BreastServiceImpl.class);
    InstanceSupplier<BreastServiceImpl> instanceSupplier = InstanceSupplier.using(BreastServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(BreastServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
