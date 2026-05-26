package com.backend.backend.service;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link PatientServiceServiceImpl}.
 */
@Generated
public class PatientServiceServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static PatientServiceServiceImpl apply(RegisteredBean registeredBean,
      PatientServiceServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("patientServiceRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
