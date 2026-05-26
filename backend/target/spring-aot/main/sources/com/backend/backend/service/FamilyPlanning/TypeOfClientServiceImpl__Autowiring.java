package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link TypeOfClientServiceImpl}.
 */
@Generated
public class TypeOfClientServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static TypeOfClientServiceImpl apply(RegisteredBean registeredBean,
      TypeOfClientServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("typeOfClientRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
