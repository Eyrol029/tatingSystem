package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link PrenatalRecordController}.
 */
@Generated
public class PrenatalRecordController__BeanDefinitions {
  /**
   * Get the bean definition for 'prenatalRecordController'.
   */
  public static BeanDefinition getPrenatalRecordControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(PrenatalRecordController.class);
    InstanceSupplier<PrenatalRecordController> instanceSupplier = InstanceSupplier.using(PrenatalRecordController::new);
    instanceSupplier = instanceSupplier.andThen(PrenatalRecordController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
