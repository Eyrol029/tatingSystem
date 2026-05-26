package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link TypeOfClientController}.
 */
@Generated
public class TypeOfClientController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static TypeOfClientController apply(RegisteredBean registeredBean,
      TypeOfClientController instance) {
    AutowiredFieldValueResolver.forRequiredField("typeOfClientService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
