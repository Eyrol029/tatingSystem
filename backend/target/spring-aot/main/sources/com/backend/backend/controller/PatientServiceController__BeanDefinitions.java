package com.backend.backend.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link PatientServiceController}.
 */
@Generated
public class PatientServiceController__BeanDefinitions {
  /**
   * Get the bean definition for 'patientServiceController'.
   */
  public static BeanDefinition getPatientServiceControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(PatientServiceController.class);
    InstanceSupplier<PatientServiceController> instanceSupplier = InstanceSupplier.using(PatientServiceController::new);
    instanceSupplier = instanceSupplier.andThen(PatientServiceController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
