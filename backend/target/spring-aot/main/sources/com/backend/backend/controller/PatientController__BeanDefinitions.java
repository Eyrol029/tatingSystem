package com.backend.backend.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link PatientController}.
 */
@Generated
public class PatientController__BeanDefinitions {
  /**
   * Get the bean definition for 'patientController'.
   */
  public static BeanDefinition getPatientControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(PatientController.class);
    InstanceSupplier<PatientController> instanceSupplier = InstanceSupplier.using(PatientController::new);
    instanceSupplier = instanceSupplier.andThen(PatientController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
