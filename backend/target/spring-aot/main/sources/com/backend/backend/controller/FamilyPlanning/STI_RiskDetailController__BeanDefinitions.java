package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link STI_RiskDetailController}.
 */
@Generated
public class STI_RiskDetailController__BeanDefinitions {
  /**
   * Get the bean definition for 'sTI_RiskDetailController'.
   */
  public static BeanDefinition getSTIRiskDetailControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(STI_RiskDetailController.class);
    InstanceSupplier<STI_RiskDetailController> instanceSupplier = InstanceSupplier.using(STI_RiskDetailController::new);
    instanceSupplier = instanceSupplier.andThen(STI_RiskDetailController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
