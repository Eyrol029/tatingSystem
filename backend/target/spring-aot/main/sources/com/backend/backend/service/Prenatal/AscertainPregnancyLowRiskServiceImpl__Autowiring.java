package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link AscertainPregnancyLowRiskServiceImpl}.
 */
@Generated
public class AscertainPregnancyLowRiskServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static AscertainPregnancyLowRiskServiceImpl apply(RegisteredBean registeredBean,
      AscertainPregnancyLowRiskServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
