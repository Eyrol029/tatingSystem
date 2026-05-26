package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link MedicalSurgicalFactorsServiceImpl}.
 */
@Generated
public class MedicalSurgicalFactorsServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static MedicalSurgicalFactorsServiceImpl apply(RegisteredBean registeredBean,
      MedicalSurgicalFactorsServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
