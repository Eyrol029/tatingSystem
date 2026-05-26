package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link PhysicalExaminationServiceImpl}.
 */
@Generated
public class PhysicalExaminationServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static PhysicalExaminationServiceImpl apply(RegisteredBean registeredBean,
      PhysicalExaminationServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("physicalExaminationRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
