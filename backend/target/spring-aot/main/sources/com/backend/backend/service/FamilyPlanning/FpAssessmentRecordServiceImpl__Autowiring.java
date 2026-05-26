package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link FpAssessmentRecordServiceImpl}.
 */
@Generated
public class FpAssessmentRecordServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static FpAssessmentRecordServiceImpl apply(RegisteredBean registeredBean,
      FpAssessmentRecordServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
