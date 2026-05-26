package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link PrenatalRecordController}.
 */
@Generated
public class PrenatalRecordController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static PrenatalRecordController apply(RegisteredBean registeredBean,
      PrenatalRecordController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
