package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link NeckController}.
 */
@Generated
public class NeckController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static NeckController apply(RegisteredBean registeredBean, NeckController instance) {
    AutowiredFieldValueResolver.forRequiredField("neckService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
