package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link MedicalHistoryDetailServiceImpl}.
 */
@Generated
public class MedicalHistoryDetailServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static MedicalHistoryDetailServiceImpl apply(RegisteredBean registeredBean,
      MedicalHistoryDetailServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("medicalHistoryDetailRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
