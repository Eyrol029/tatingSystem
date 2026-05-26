package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link SkinServiceImpl}.
 */
@Generated
public class SkinServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'skinServiceImpl'.
   */
  public static BeanDefinition getSkinServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(SkinServiceImpl.class);
    InstanceSupplier<SkinServiceImpl> instanceSupplier = InstanceSupplier.using(SkinServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(SkinServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
