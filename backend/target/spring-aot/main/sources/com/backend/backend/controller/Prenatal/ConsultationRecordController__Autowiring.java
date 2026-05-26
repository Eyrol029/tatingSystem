package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ConsultationRecordController}.
 */
@Generated
public class ConsultationRecordController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ConsultationRecordController apply(RegisteredBean registeredBean,
      ConsultationRecordController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
