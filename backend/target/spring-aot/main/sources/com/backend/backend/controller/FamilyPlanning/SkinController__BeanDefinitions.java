package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link SkinController}.
 */
@Generated
public class SkinController__BeanDefinitions {
  /**
   * Get the bean definition for 'skinController'.
   */
  public static BeanDefinition getSkinControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(SkinController.class);
    InstanceSupplier<SkinController> instanceSupplier = InstanceSupplier.using(SkinController::new);
    instanceSupplier = instanceSupplier.andThen(SkinController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
