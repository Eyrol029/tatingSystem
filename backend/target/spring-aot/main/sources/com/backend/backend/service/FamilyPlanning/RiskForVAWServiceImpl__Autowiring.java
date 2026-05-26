package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link RiskForVAWServiceImpl}.
 */
@Generated
public class RiskForVAWServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static RiskForVAWServiceImpl apply(RegisteredBean registeredBean,
      RiskForVAWServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("riskForVAWRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
