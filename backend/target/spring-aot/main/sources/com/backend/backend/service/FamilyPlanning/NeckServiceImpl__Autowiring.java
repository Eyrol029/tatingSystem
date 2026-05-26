package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link NeckServiceImpl}.
 */
@Generated
public class NeckServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static NeckServiceImpl apply(RegisteredBean registeredBean, NeckServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("neckRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
