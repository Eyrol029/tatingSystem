package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link STI_ConditionController}.
 */
@Generated
public class STI_ConditionController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static STI_ConditionController apply(RegisteredBean registeredBean,
      STI_ConditionController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
