package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link CompleteDiagnosisController}.
 */
@Generated
public class CompleteDiagnosisController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static CompleteDiagnosisController apply(RegisteredBean registeredBean,
      CompleteDiagnosisController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
