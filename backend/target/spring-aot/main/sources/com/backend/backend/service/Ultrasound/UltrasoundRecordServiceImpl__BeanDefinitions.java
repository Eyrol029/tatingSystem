package com.backend.backend.service.Ultrasound;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link UltrasoundRecordServiceImpl}.
 */
@Generated
public class UltrasoundRecordServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'ultrasoundRecordServiceImpl'.
   */
  public static BeanDefinition getUltrasoundRecordServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(UltrasoundRecordServiceImpl.class);
    InstanceSupplier<UltrasoundRecordServiceImpl> instanceSupplier = InstanceSupplier.using(UltrasoundRecordServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(UltrasoundRecordServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
