package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ObstetricalHistoryController}.
 */
@Generated
public class ObstetricalHistoryController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ObstetricalHistoryController apply(RegisteredBean registeredBean,
      ObstetricalHistoryController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
