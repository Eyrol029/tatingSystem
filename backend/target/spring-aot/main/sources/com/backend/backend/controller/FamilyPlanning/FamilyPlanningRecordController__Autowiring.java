package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link FamilyPlanningRecordController}.
 */
@Generated
public class FamilyPlanningRecordController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static FamilyPlanningRecordController apply(RegisteredBean registeredBean,
      FamilyPlanningRecordController instance) {
    AutowiredFieldValueResolver.forRequiredField("familyPlanningRecordService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
