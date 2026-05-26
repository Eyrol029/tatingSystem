package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link MedicalSurgicalFactorsController}.
 */
@Generated
public class MedicalSurgicalFactorsController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static MedicalSurgicalFactorsController apply(RegisteredBean registeredBean,
      MedicalSurgicalFactorsController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
