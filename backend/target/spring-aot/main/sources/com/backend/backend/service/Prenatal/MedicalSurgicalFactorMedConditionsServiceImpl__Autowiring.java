package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link MedicalSurgicalFactorMedConditionsServiceImpl}.
 */
@Generated
public class MedicalSurgicalFactorMedConditionsServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static MedicalSurgicalFactorMedConditionsServiceImpl apply(RegisteredBean registeredBean,
      MedicalSurgicalFactorMedConditionsServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
