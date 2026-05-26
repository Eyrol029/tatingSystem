package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ConjunctivaController}.
 */
@Generated
public class ConjunctivaController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ConjunctivaController apply(RegisteredBean registeredBean,
      ConjunctivaController instance) {
    AutowiredFieldValueResolver.forRequiredField("conjunctivaService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
