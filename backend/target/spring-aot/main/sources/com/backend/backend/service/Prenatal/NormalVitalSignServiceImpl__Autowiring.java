package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link NormalVitalSignServiceImpl}.
 */
@Generated
public class NormalVitalSignServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static NormalVitalSignServiceImpl apply(RegisteredBean registeredBean,
      NormalVitalSignServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
