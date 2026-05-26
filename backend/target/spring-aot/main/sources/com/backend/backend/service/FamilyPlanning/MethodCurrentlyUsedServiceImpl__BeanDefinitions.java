package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link MethodCurrentlyUsedServiceImpl}.
 */
@Generated
public class MethodCurrentlyUsedServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'methodCurrentlyUsedServiceImpl'.
   */
  public static BeanDefinition getMethodCurrentlyUsedServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MethodCurrentlyUsedServiceImpl.class);
    InstanceSupplier<MethodCurrentlyUsedServiceImpl> instanceSupplier = InstanceSupplier.using(MethodCurrentlyUsedServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(MethodCurrentlyUsedServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
