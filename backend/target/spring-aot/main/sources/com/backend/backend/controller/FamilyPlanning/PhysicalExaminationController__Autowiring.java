package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link PhysicalExaminationController}.
 */
@Generated
public class PhysicalExaminationController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static PhysicalExaminationController apply(RegisteredBean registeredBean,
      PhysicalExaminationController instance) {
    AutowiredFieldValueResolver.forRequiredField("physicalExaminationService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
