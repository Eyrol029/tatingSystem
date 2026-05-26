package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link LaboratoryResultsServiceImpl}.
 */
@Generated
public class LaboratoryResultsServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'laboratoryResultsServiceImpl'.
   */
  public static BeanDefinition getLaboratoryResultsServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(LaboratoryResultsServiceImpl.class);
    InstanceSupplier<LaboratoryResultsServiceImpl> instanceSupplier = InstanceSupplier.using(LaboratoryResultsServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(LaboratoryResultsServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
