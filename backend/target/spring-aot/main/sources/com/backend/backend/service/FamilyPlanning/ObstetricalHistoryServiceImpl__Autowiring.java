package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ObstetricalHistoryServiceImpl}.
 */
@Generated
public class ObstetricalHistoryServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ObstetricalHistoryServiceImpl apply(RegisteredBean registeredBean,
      ObstetricalHistoryServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
