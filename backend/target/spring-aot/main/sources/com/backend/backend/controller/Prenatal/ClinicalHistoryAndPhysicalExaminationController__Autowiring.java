package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ClinicalHistoryAndPhysicalExaminationController}.
 */
@Generated
public class ClinicalHistoryAndPhysicalExaminationController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ClinicalHistoryAndPhysicalExaminationController apply(RegisteredBean registeredBean,
      ClinicalHistoryAndPhysicalExaminationController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
