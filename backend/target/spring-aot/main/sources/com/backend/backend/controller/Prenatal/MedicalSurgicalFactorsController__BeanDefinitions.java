package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link MedicalSurgicalFactorsController}.
 */
@Generated
public class MedicalSurgicalFactorsController__BeanDefinitions {
  /**
   * Get the bean definition for 'medicalSurgicalFactorsController'.
   */
  public static BeanDefinition getMedicalSurgicalFactorsControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MedicalSurgicalFactorsController.class);
    InstanceSupplier<MedicalSurgicalFactorsController> instanceSupplier = InstanceSupplier.using(MedicalSurgicalFactorsController::new);
    instanceSupplier = instanceSupplier.andThen(MedicalSurgicalFactorsController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
