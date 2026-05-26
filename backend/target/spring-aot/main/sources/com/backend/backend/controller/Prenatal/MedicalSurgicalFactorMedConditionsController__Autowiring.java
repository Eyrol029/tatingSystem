package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link MedicalSurgicalFactorMedConditionsController}.
 */
@Generated
public class MedicalSurgicalFactorMedConditionsController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static MedicalSurgicalFactorMedConditionsController apply(RegisteredBean registeredBean,
      MedicalSurgicalFactorMedConditionsController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
