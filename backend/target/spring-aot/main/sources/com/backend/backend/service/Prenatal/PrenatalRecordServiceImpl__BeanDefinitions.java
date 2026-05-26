package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link PrenatalRecordServiceImpl}.
 */
@Generated
public class PrenatalRecordServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'prenatalRecordServiceImpl'.
   */
  public static BeanDefinition getPrenatalRecordServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(PrenatalRecordServiceImpl.class);
    InstanceSupplier<PrenatalRecordServiceImpl> instanceSupplier = InstanceSupplier.using(PrenatalRecordServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(PrenatalRecordServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
