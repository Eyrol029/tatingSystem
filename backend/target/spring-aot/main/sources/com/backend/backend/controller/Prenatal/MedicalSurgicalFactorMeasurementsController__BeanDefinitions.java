package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link MedicalSurgicalFactorMeasurementsController}.
 */
@Generated
public class MedicalSurgicalFactorMeasurementsController__BeanDefinitions {
  /**
   * Get the bean definition for 'medicalSurgicalFactorMeasurementsController'.
   */
  public static BeanDefinition getMedicalSurgicalFactorMeasurementsControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MedicalSurgicalFactorMeasurementsController.class);
    InstanceSupplier<MedicalSurgicalFactorMeasurementsController> instanceSupplier = InstanceSupplier.using(MedicalSurgicalFactorMeasurementsController::new);
    instanceSupplier = instanceSupplier.andThen(MedicalSurgicalFactorMeasurementsController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
