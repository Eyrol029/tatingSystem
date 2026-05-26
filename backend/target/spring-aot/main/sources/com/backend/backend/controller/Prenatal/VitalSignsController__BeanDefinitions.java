package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link VitalSignsController}.
 */
@Generated
public class VitalSignsController__BeanDefinitions {
  /**
   * Get the bean definition for 'vitalSignsController'.
   */
  public static BeanDefinition getVitalSignsControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(VitalSignsController.class);
    InstanceSupplier<VitalSignsController> instanceSupplier = InstanceSupplier.using(VitalSignsController::new);
    instanceSupplier = instanceSupplier.andThen(VitalSignsController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
