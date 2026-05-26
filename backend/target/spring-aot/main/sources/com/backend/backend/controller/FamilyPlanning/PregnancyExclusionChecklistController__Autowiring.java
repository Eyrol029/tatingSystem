package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link PregnancyExclusionChecklistController}.
 */
@Generated
public class PregnancyExclusionChecklistController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static PregnancyExclusionChecklistController apply(RegisteredBean registeredBean,
      PregnancyExclusionChecklistController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
