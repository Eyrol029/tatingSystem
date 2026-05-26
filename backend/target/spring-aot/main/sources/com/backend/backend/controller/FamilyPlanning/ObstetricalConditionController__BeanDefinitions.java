package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ObstetricalConditionController}.
 */
@Generated
public class ObstetricalConditionController__BeanDefinitions {
  /**
   * Get the bean definition for 'obstetricalConditionController'.
   */
  public static BeanDefinition getObstetricalConditionControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ObstetricalConditionController.class);
    InstanceSupplier<ObstetricalConditionController> instanceSupplier = InstanceSupplier.using(ObstetricalConditionController::new);
    instanceSupplier = instanceSupplier.andThen(ObstetricalConditionController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
