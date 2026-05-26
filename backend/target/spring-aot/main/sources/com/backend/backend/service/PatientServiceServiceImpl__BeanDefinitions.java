package com.backend.backend.service;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link PatientServiceServiceImpl}.
 */
@Generated
public class PatientServiceServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'patientServiceServiceImpl'.
   */
  public static BeanDefinition getPatientServiceServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(PatientServiceServiceImpl.class);
    InstanceSupplier<PatientServiceServiceImpl> instanceSupplier = InstanceSupplier.using(PatientServiceServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(PatientServiceServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
