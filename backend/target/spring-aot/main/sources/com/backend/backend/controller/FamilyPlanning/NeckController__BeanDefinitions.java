package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link NeckController}.
 */
@Generated
public class NeckController__BeanDefinitions {
  /**
   * Get the bean definition for 'neckController'.
   */
  public static BeanDefinition getNeckControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(NeckController.class);
    InstanceSupplier<NeckController> instanceSupplier = InstanceSupplier.using(NeckController::new);
    instanceSupplier = instanceSupplier.andThen(NeckController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
