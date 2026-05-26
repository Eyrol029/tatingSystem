package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link MedicalConditionController}.
 */
@Generated
public class MedicalConditionController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static MedicalConditionController apply(RegisteredBean registeredBean,
      MedicalConditionController instance) {
    AutowiredFieldValueResolver.forRequiredField("medicalConditionService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
