package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ObstetricalConditionController}.
 */
@Generated
public class ObstetricalConditionController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ObstetricalConditionController apply(RegisteredBean registeredBean,
      ObstetricalConditionController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
