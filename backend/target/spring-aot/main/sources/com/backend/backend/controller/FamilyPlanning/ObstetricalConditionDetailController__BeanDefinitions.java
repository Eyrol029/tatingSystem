package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ObstetricalConditionDetailController}.
 */
@Generated
public class ObstetricalConditionDetailController__BeanDefinitions {
  /**
   * Get the bean definition for 'obstetricalConditionDetailController'.
   */
  public static BeanDefinition getObstetricalConditionDetailControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ObstetricalConditionDetailController.class);
    InstanceSupplier<ObstetricalConditionDetailController> instanceSupplier = InstanceSupplier.using(ObstetricalConditionDetailController::new);
    instanceSupplier = instanceSupplier.andThen(ObstetricalConditionDetailController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
