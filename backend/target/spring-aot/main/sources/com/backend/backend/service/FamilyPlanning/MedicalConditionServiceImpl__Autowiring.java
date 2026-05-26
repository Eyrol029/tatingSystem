package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link MedicalConditionServiceImpl}.
 */
@Generated
public class MedicalConditionServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static MedicalConditionServiceImpl apply(RegisteredBean registeredBean,
      MedicalConditionServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("medicalConditionRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
