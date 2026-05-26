package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link PrenatalRecordServiceImpl}.
 */
@Generated
public class PrenatalRecordServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static PrenatalRecordServiceImpl apply(RegisteredBean registeredBean,
      PrenatalRecordServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
