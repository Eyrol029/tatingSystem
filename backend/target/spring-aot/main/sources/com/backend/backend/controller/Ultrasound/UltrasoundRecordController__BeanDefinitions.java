package com.backend.backend.controller.Ultrasound;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link UltrasoundRecordController}.
 */
@Generated
public class UltrasoundRecordController__BeanDefinitions {
  /**
   * Get the bean definition for 'ultrasoundRecordController'.
   */
  public static BeanDefinition getUltrasoundRecordControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(UltrasoundRecordController.class);
    InstanceSupplier<UltrasoundRecordController> instanceSupplier = InstanceSupplier.using(UltrasoundRecordController::new);
    instanceSupplier = instanceSupplier.andThen(UltrasoundRecordController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
