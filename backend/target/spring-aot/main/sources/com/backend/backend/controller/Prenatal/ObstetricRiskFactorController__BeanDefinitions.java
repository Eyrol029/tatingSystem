package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ObstetricRiskFactorController}.
 */
@Generated
public class ObstetricRiskFactorController__BeanDefinitions {
  /**
   * Get the bean definition for 'obstetricRiskFactorController'.
   */
  public static BeanDefinition getObstetricRiskFactorControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ObstetricRiskFactorController.class);
    InstanceSupplier<ObstetricRiskFactorController> instanceSupplier = InstanceSupplier.using(ObstetricRiskFactorController::new);
    instanceSupplier = instanceSupplier.andThen(ObstetricRiskFactorController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
