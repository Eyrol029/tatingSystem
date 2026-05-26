package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link AscertainPregnancyLowRiskController}.
 */
@Generated
public class AscertainPregnancyLowRiskController__BeanDefinitions {
  /**
   * Get the bean definition for 'ascertainPregnancyLowRiskController'.
   */
  public static BeanDefinition getAscertainPregnancyLowRiskControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(AscertainPregnancyLowRiskController.class);
    InstanceSupplier<AscertainPregnancyLowRiskController> instanceSupplier = InstanceSupplier.using(AscertainPregnancyLowRiskController::new);
    instanceSupplier = instanceSupplier.andThen(AscertainPregnancyLowRiskController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
