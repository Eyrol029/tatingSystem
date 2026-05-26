package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ObstetricalConditionDetailController}.
 */
@Generated
public class ObstetricalConditionDetailController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ObstetricalConditionDetailController apply(RegisteredBean registeredBean,
      ObstetricalConditionDetailController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
