package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link TreatmentManagementServiceImpl}.
 */
@Generated
public class TreatmentManagementServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static TreatmentManagementServiceImpl apply(RegisteredBean registeredBean,
      TreatmentManagementServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
