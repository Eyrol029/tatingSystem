package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link VitalSignsServiceImpl}.
 */
@Generated
public class VitalSignsServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'vitalSignsServiceImpl'.
   */
  public static BeanDefinition getVitalSignsServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(VitalSignsServiceImpl.class);
    InstanceSupplier<VitalSignsServiceImpl> instanceSupplier = InstanceSupplier.using(VitalSignsServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(VitalSignsServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
