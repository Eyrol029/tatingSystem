package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link PelvicExaminationController}.
 */
@Generated
public class PelvicExaminationController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static PelvicExaminationController apply(RegisteredBean registeredBean,
      PelvicExaminationController instance) {
    AutowiredFieldValueResolver.forRequiredField("pelvicExaminationService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
