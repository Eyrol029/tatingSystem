package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ClinicalHistoryAndPhysicalExaminationServiceImpl}.
 */
@Generated
public class ClinicalHistoryAndPhysicalExaminationServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ClinicalHistoryAndPhysicalExaminationServiceImpl apply(
      RegisteredBean registeredBean, ClinicalHistoryAndPhysicalExaminationServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
