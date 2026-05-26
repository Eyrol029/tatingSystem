package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ConjunctivaServiceImpl}.
 */
@Generated
public class ConjunctivaServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ConjunctivaServiceImpl apply(RegisteredBean registeredBean,
      ConjunctivaServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("conjunctivaRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
