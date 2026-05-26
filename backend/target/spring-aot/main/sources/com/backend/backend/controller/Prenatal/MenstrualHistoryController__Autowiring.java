package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link MenstrualHistoryController}.
 */
@Generated
public class MenstrualHistoryController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static MenstrualHistoryController apply(RegisteredBean registeredBean,
      MenstrualHistoryController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
