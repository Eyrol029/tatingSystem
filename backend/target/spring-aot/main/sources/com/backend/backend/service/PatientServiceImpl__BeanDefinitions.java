package com.backend.backend.service;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link PatientServiceImpl}.
 */
@Generated
public class PatientServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'patientServiceImpl'.
   */
  public static BeanDefinition getPatientServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(PatientServiceImpl.class);
    InstanceSupplier<PatientServiceImpl> instanceSupplier = InstanceSupplier.using(PatientServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(PatientServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
