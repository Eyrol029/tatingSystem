package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link MethodCurrentlyUsedController}.
 */
@Generated
public class MethodCurrentlyUsedController__BeanDefinitions {
  /**
   * Get the bean definition for 'methodCurrentlyUsedController'.
   */
  public static BeanDefinition getMethodCurrentlyUsedControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MethodCurrentlyUsedController.class);
    InstanceSupplier<MethodCurrentlyUsedController> instanceSupplier = InstanceSupplier.using(MethodCurrentlyUsedController::new);
    instanceSupplier = instanceSupplier.andThen(MethodCurrentlyUsedController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
