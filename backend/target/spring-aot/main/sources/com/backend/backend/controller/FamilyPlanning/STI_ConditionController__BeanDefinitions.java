package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link STI_ConditionController}.
 */
@Generated
public class STI_ConditionController__BeanDefinitions {
  /**
   * Get the bean definition for 'sTI_ConditionController'.
   */
  public static BeanDefinition getSTIConditionControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(STI_ConditionController.class);
    InstanceSupplier<STI_ConditionController> instanceSupplier = InstanceSupplier.using(STI_ConditionController::new);
    instanceSupplier = instanceSupplier.andThen(STI_ConditionController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
