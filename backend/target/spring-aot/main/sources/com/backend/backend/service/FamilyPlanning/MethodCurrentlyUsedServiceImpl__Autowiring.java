package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link MethodCurrentlyUsedServiceImpl}.
 */
@Generated
public class MethodCurrentlyUsedServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static MethodCurrentlyUsedServiceImpl apply(RegisteredBean registeredBean,
      MethodCurrentlyUsedServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("methodCurrentlyUsedRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
