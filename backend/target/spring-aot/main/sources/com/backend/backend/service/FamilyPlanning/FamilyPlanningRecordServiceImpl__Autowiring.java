package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link FamilyPlanningRecordServiceImpl}.
 */
@Generated
public class FamilyPlanningRecordServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static FamilyPlanningRecordServiceImpl apply(RegisteredBean registeredBean,
      FamilyPlanningRecordServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("familyPlanningRecordRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
