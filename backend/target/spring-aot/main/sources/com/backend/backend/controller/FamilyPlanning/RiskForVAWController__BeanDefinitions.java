package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link RiskForVAWController}.
 */
@Generated
public class RiskForVAWController__BeanDefinitions {
  /**
   * Get the bean definition for 'riskForVAWController'.
   */
  public static BeanDefinition getRiskForVAWControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(RiskForVAWController.class);
    InstanceSupplier<RiskForVAWController> instanceSupplier = InstanceSupplier.using(RiskForVAWController::new);
    instanceSupplier = instanceSupplier.andThen(RiskForVAWController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
