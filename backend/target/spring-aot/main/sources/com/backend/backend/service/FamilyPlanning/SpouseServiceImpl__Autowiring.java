package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link SpouseServiceImpl}.
 */
@Generated
public class SpouseServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static SpouseServiceImpl apply(RegisteredBean registeredBean, SpouseServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("spouseRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
