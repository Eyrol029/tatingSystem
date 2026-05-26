package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ConjunctivaServiceImpl}.
 */
@Generated
public class ConjunctivaServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'conjunctivaServiceImpl'.
   */
  public static BeanDefinition getConjunctivaServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ConjunctivaServiceImpl.class);
    InstanceSupplier<ConjunctivaServiceImpl> instanceSupplier = InstanceSupplier.using(ConjunctivaServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(ConjunctivaServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
