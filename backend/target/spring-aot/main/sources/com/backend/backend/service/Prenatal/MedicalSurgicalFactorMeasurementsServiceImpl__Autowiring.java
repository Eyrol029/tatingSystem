package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link MedicalSurgicalFactorMeasurementsServiceImpl}.
 */
@Generated
public class MedicalSurgicalFactorMeasurementsServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static MedicalSurgicalFactorMeasurementsServiceImpl apply(RegisteredBean registeredBean,
      MedicalSurgicalFactorMeasurementsServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
