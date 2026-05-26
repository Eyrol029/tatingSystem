package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link SpouseController}.
 */
@Generated
public class SpouseController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static SpouseController apply(RegisteredBean registeredBean, SpouseController instance) {
    AutowiredFieldValueResolver.forRequiredField("spouseService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
