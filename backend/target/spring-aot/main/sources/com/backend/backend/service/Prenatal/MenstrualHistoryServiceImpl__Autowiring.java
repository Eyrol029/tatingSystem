package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link MenstrualHistoryServiceImpl}.
 */
@Generated
public class MenstrualHistoryServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static MenstrualHistoryServiceImpl apply(RegisteredBean registeredBean,
      MenstrualHistoryServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
