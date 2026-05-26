package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link AscertainPregnancyLowRiskController}.
 */
@Generated
public class AscertainPregnancyLowRiskController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static AscertainPregnancyLowRiskController apply(RegisteredBean registeredBean,
      AscertainPregnancyLowRiskController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
