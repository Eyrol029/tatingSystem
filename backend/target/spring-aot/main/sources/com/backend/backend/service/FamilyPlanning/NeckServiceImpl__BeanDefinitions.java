package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link NeckServiceImpl}.
 */
@Generated
public class NeckServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'neckServiceImpl'.
   */
  public static BeanDefinition getNeckServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(NeckServiceImpl.class);
    InstanceSupplier<NeckServiceImpl> instanceSupplier = InstanceSupplier.using(NeckServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(NeckServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
