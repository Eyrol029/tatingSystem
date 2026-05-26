package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link MedicalHistoryServiceImpl}.
 */
@Generated
public class MedicalHistoryServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static MedicalHistoryServiceImpl apply(RegisteredBean registeredBean,
      MedicalHistoryServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("medicalHistoryRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
