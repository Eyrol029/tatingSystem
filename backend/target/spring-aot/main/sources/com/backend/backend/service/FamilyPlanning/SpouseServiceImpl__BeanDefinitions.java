package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link SpouseServiceImpl}.
 */
@Generated
public class SpouseServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'spouseServiceImpl'.
   */
  public static BeanDefinition getSpouseServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(SpouseServiceImpl.class);
    InstanceSupplier<SpouseServiceImpl> instanceSupplier = InstanceSupplier.using(SpouseServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(SpouseServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
