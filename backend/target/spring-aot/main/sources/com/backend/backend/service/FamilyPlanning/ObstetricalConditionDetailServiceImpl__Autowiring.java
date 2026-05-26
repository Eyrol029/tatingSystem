package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ObstetricalConditionDetailServiceImpl}.
 */
@Generated
public class ObstetricalConditionDetailServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ObstetricalConditionDetailServiceImpl apply(RegisteredBean registeredBean,
      ObstetricalConditionDetailServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
