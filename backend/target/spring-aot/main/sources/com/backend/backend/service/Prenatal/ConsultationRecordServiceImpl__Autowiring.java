package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ConsultationRecordServiceImpl}.
 */
@Generated
public class ConsultationRecordServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ConsultationRecordServiceImpl apply(RegisteredBean registeredBean,
      ConsultationRecordServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
