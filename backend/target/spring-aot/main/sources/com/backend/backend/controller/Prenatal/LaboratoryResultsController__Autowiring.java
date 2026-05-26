package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link LaboratoryResultsController}.
 */
@Generated
public class LaboratoryResultsController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static LaboratoryResultsController apply(RegisteredBean registeredBean,
      LaboratoryResultsController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
