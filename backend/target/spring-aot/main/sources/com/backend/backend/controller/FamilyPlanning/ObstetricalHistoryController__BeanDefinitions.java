package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ObstetricalHistoryController}.
 */
@Generated
public class ObstetricalHistoryController__BeanDefinitions {
  /**
   * Get the bean definition for 'obstetricalHistoryController'.
   */
  public static BeanDefinition getObstetricalHistoryControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ObstetricalHistoryController.class);
    InstanceSupplier<ObstetricalHistoryController> instanceSupplier = InstanceSupplier.using(ObstetricalHistoryController::new);
    instanceSupplier = instanceSupplier.andThen(ObstetricalHistoryController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
