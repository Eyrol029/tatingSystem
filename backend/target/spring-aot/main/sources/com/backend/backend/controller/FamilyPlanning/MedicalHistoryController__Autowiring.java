package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link MedicalHistoryController}.
 */
@Generated
public class MedicalHistoryController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static MedicalHistoryController apply(RegisteredBean registeredBean,
      MedicalHistoryController instance) {
    AutowiredFieldValueResolver.forRequiredField("medicalHistoryService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
