package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link MedicalSurgicalFactorMedConditionsController}.
 */
@Generated
public class MedicalSurgicalFactorMedConditionsController__BeanDefinitions {
  /**
   * Get the bean definition for 'medicalSurgicalFactorMedConditionsController'.
   */
  public static BeanDefinition getMedicalSurgicalFactorMedConditionsControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MedicalSurgicalFactorMedConditionsController.class);
    InstanceSupplier<MedicalSurgicalFactorMedConditionsController> instanceSupplier = InstanceSupplier.using(MedicalSurgicalFactorMedConditionsController::new);
    instanceSupplier = instanceSupplier.andThen(MedicalSurgicalFactorMedConditionsController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
