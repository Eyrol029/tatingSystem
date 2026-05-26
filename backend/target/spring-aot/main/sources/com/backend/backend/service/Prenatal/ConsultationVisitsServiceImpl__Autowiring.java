package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ConsultationVisitsServiceImpl}.
 */
@Generated
public class ConsultationVisitsServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ConsultationVisitsServiceImpl apply(RegisteredBean registeredBean,
      ConsultationVisitsServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
