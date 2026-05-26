package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link BreastController}.
 */
@Generated
public class BreastController__BeanDefinitions {
  /**
   * Get the bean definition for 'breastController'.
   */
  public static BeanDefinition getBreastControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(BreastController.class);
    InstanceSupplier<BreastController> instanceSupplier = InstanceSupplier.using(BreastController::new);
    instanceSupplier = instanceSupplier.andThen(BreastController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
