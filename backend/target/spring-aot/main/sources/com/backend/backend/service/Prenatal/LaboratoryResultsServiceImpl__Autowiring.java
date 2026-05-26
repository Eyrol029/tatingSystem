package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link LaboratoryResultsServiceImpl}.
 */
@Generated
public class LaboratoryResultsServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static LaboratoryResultsServiceImpl apply(RegisteredBean registeredBean,
      LaboratoryResultsServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
