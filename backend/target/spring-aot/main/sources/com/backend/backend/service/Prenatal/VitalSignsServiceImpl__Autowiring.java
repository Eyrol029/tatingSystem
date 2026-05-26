package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link VitalSignsServiceImpl}.
 */
@Generated
public class VitalSignsServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static VitalSignsServiceImpl apply(RegisteredBean registeredBean,
      VitalSignsServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
