package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link STI_RiskDetailServiceImpl}.
 */
@Generated
public class STI_RiskDetailServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static STI_RiskDetailServiceImpl apply(RegisteredBean registeredBean,
      STI_RiskDetailServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
