package com.backend.backend.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link PatientServiceController}.
 */
@Generated
public class PatientServiceController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static PatientServiceController apply(RegisteredBean registeredBean,
      PatientServiceController instance) {
    AutowiredFieldValueResolver.forRequiredField("patientServiceService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
