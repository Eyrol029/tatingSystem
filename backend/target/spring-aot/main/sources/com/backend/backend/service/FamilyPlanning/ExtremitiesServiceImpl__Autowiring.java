package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ExtremitiesServiceImpl}.
 */
@Generated
public class ExtremitiesServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ExtremitiesServiceImpl apply(RegisteredBean registeredBean,
      ExtremitiesServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("extremitiesRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
