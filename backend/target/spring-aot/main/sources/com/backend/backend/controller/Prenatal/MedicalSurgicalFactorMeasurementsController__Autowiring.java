package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link MedicalSurgicalFactorMeasurementsController}.
 */
@Generated
public class MedicalSurgicalFactorMeasurementsController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static MedicalSurgicalFactorMeasurementsController apply(RegisteredBean registeredBean,
      MedicalSurgicalFactorMeasurementsController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
