package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link NormalVitalSignController}.
 */
@Generated
public class NormalVitalSignController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static NormalVitalSignController apply(RegisteredBean registeredBean,
      NormalVitalSignController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
