package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ExtremitiesController}.
 */
@Generated
public class ExtremitiesController__BeanDefinitions {
  /**
   * Get the bean definition for 'extremitiesController'.
   */
  public static BeanDefinition getExtremitiesControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ExtremitiesController.class);
    InstanceSupplier<ExtremitiesController> instanceSupplier = InstanceSupplier.using(ExtremitiesController::new);
    instanceSupplier = instanceSupplier.andThen(ExtremitiesController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
