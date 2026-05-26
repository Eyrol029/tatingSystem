package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ObstetricRiskFactorServiceImpl}.
 */
@Generated
public class ObstetricRiskFactorServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ObstetricRiskFactorServiceImpl apply(RegisteredBean registeredBean,
      ObstetricRiskFactorServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
