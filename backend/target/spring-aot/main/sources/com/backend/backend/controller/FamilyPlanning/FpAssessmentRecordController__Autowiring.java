package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link FpAssessmentRecordController}.
 */
@Generated
public class FpAssessmentRecordController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static FpAssessmentRecordController apply(RegisteredBean registeredBean,
      FpAssessmentRecordController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
