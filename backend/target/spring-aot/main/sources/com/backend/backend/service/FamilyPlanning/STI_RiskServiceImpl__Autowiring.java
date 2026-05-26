package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link STI_RiskServiceImpl}.
 */
@Generated
public class STI_RiskServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static STI_RiskServiceImpl apply(RegisteredBean registeredBean,
      STI_RiskServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
