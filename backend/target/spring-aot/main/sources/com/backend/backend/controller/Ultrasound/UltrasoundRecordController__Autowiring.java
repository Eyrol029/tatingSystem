package com.backend.backend.controller.Ultrasound;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link UltrasoundRecordController}.
 */
@Generated
public class UltrasoundRecordController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static UltrasoundRecordController apply(RegisteredBean registeredBean,
      UltrasoundRecordController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
