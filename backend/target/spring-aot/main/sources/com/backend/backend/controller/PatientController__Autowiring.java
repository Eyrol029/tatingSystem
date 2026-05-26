package com.backend.backend.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link PatientController}.
 */
@Generated
public class PatientController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static PatientController apply(RegisteredBean registeredBean, PatientController instance) {
    AutowiredFieldValueResolver.forRequiredField("patientService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
