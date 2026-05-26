package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link PelvicExaminationServiceImpl}.
 */
@Generated
public class PelvicExaminationServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static PelvicExaminationServiceImpl apply(RegisteredBean registeredBean,
      PelvicExaminationServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("pelvicExaminationRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
