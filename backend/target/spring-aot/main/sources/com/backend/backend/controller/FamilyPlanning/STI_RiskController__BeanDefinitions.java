package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link STI_RiskController}.
 */
@Generated
public class STI_RiskController__BeanDefinitions {
  /**
   * Get the bean definition for 'sTI_RiskController'.
   */
  public static BeanDefinition getSTIRiskControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(STI_RiskController.class);
    InstanceSupplier<STI_RiskController> instanceSupplier = InstanceSupplier.using(STI_RiskController::new);
    instanceSupplier = instanceSupplier.andThen(STI_RiskController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
