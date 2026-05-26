package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ExtremitiesController}.
 */
@Generated
public class ExtremitiesController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ExtremitiesController apply(RegisteredBean registeredBean,
      ExtremitiesController instance) {
    AutowiredFieldValueResolver.forRequiredField("extremitiesService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
