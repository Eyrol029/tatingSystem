package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link SpouseController}.
 */
@Generated
public class SpouseController__BeanDefinitions {
  /**
   * Get the bean definition for 'spouseController'.
   */
  public static BeanDefinition getSpouseControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(SpouseController.class);
    InstanceSupplier<SpouseController> instanceSupplier = InstanceSupplier.using(SpouseController::new);
    instanceSupplier = instanceSupplier.andThen(SpouseController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
