package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ObstetricRiskFactorController}.
 */
@Generated
public class ObstetricRiskFactorController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ObstetricRiskFactorController apply(RegisteredBean registeredBean,
      ObstetricRiskFactorController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
