package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link VitalSignsController}.
 */
@Generated
public class VitalSignsController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static VitalSignsController apply(RegisteredBean registeredBean,
      VitalSignsController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
