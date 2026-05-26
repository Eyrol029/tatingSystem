package com.backend.backend.service;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link PatientServiceImpl}.
 */
@Generated
public class PatientServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static PatientServiceImpl apply(RegisteredBean registeredBean,
      PatientServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("patientRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
