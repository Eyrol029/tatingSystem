package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link MedicalHistoryDetailController}.
 */
@Generated
public class MedicalHistoryDetailController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static MedicalHistoryDetailController apply(RegisteredBean registeredBean,
      MedicalHistoryDetailController instance) {
    AutowiredFieldValueResolver.forRequiredField("medicalHistoryDetailService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
