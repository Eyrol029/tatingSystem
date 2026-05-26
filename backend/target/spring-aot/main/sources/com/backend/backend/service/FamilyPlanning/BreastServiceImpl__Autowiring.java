package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link BreastServiceImpl}.
 */
@Generated
public class BreastServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static BreastServiceImpl apply(RegisteredBean registeredBean, BreastServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("breastRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
