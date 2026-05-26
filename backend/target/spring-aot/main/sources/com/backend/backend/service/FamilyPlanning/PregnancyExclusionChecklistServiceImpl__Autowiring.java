package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link PregnancyExclusionChecklistServiceImpl}.
 */
@Generated
public class PregnancyExclusionChecklistServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static PregnancyExclusionChecklistServiceImpl apply(RegisteredBean registeredBean,
      PregnancyExclusionChecklistServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
