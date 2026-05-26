package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ObstetricalConditionServiceImpl}.
 */
@Generated
public class ObstetricalConditionServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ObstetricalConditionServiceImpl apply(RegisteredBean registeredBean,
      ObstetricalConditionServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
