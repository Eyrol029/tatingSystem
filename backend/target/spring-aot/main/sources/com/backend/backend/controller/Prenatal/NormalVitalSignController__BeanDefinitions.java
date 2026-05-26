package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link NormalVitalSignController}.
 */
@Generated
public class NormalVitalSignController__BeanDefinitions {
  /**
   * Get the bean definition for 'normalVitalSignController'.
   */
  public static BeanDefinition getNormalVitalSignControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(NormalVitalSignController.class);
    InstanceSupplier<NormalVitalSignController> instanceSupplier = InstanceSupplier.using(NormalVitalSignController::new);
    instanceSupplier = instanceSupplier.andThen(NormalVitalSignController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
