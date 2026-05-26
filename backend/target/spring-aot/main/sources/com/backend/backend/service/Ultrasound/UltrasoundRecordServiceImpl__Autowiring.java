package com.backend.backend.service.Ultrasound;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link UltrasoundRecordServiceImpl}.
 */
@Generated
public class UltrasoundRecordServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static UltrasoundRecordServiceImpl apply(RegisteredBean registeredBean,
      UltrasoundRecordServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
