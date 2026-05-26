package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link SkinServiceImpl}.
 */
@Generated
public class SkinServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static SkinServiceImpl apply(RegisteredBean registeredBean, SkinServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("skinRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
