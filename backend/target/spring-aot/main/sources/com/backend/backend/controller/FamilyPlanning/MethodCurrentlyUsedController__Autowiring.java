package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link MethodCurrentlyUsedController}.
 */
@Generated
public class MethodCurrentlyUsedController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static MethodCurrentlyUsedController apply(RegisteredBean registeredBean,
      MethodCurrentlyUsedController instance) {
    AutowiredFieldValueResolver.forRequiredField("methodCurrentlyUsedService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
