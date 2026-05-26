package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link AbdomenServiceImpl}.
 */
@Generated
public class AbdomenServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'abdomenServiceImpl'.
   */
  public static BeanDefinition getAbdomenServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(AbdomenServiceImpl.class);
    InstanceSupplier<AbdomenServiceImpl> instanceSupplier = InstanceSupplier.using(AbdomenServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(AbdomenServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
