package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ClientController}.
 */
@Generated
public class ClientController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ClientController apply(RegisteredBean registeredBean, ClientController instance) {
    AutowiredFieldValueResolver.forRequiredField("clientService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
