package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link RiskForVAWController}.
 */
@Generated
public class RiskForVAWController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static RiskForVAWController apply(RegisteredBean registeredBean,
      RiskForVAWController instance) {
    AutowiredFieldValueResolver.forRequiredField("riskForVAWService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
