package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link SkinController}.
 */
@Generated
public class SkinController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static SkinController apply(RegisteredBean registeredBean, SkinController instance) {
    AutowiredFieldValueResolver.forRequiredField("skinService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
