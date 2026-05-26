package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ClientServiceImpl}.
 */
@Generated
public class ClientServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ClientServiceImpl apply(RegisteredBean registeredBean, ClientServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("clientRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
