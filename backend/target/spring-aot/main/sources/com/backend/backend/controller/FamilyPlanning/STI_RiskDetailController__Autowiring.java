package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link STI_RiskDetailController}.
 */
@Generated
public class STI_RiskDetailController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static STI_RiskDetailController apply(RegisteredBean registeredBean,
      STI_RiskDetailController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
