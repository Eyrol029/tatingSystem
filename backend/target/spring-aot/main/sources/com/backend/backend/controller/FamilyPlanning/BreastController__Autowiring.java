package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link BreastController}.
 */
@Generated
public class BreastController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static BreastController apply(RegisteredBean registeredBean, BreastController instance) {
    AutowiredFieldValueResolver.forRequiredField("breastService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
