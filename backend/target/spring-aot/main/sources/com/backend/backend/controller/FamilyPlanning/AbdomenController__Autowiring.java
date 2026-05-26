package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link AbdomenController}.
 */
@Generated
public class AbdomenController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static AbdomenController apply(RegisteredBean registeredBean, AbdomenController instance) {
    AutowiredFieldValueResolver.forRequiredField("abdomenService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
