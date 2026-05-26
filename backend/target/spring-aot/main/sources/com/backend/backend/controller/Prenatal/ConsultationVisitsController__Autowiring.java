package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ConsultationVisitsController}.
 */
@Generated
public class ConsultationVisitsController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ConsultationVisitsController apply(RegisteredBean registeredBean,
      ConsultationVisitsController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
