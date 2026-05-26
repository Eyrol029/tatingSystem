package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link STI_ConditionServiceImpl}.
 */
@Generated
public class STI_ConditionServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static STI_ConditionServiceImpl apply(RegisteredBean registeredBean,
      STI_ConditionServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
