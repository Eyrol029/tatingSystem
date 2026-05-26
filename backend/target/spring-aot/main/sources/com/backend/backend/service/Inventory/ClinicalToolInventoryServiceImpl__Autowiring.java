package com.backend.backend.service.Inventory;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ClinicalToolInventoryServiceImpl}.
 */
@Generated
public class ClinicalToolInventoryServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ClinicalToolInventoryServiceImpl apply(RegisteredBean registeredBean,
      ClinicalToolInventoryServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("clinicalToolInventoryRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
