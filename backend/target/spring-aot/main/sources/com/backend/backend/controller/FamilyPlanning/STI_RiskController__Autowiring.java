package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link STI_RiskController}.
 */
@Generated
public class STI_RiskController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static STI_RiskController apply(RegisteredBean registeredBean,
      STI_RiskController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
