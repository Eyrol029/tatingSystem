package com.backend.backend.controller.Inventory;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ClinicalToolInventoryController}.
 */
@Generated
public class ClinicalToolInventoryController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ClinicalToolInventoryController apply(RegisteredBean registeredBean,
      ClinicalToolInventoryController instance) {
    AutowiredFieldValueResolver.forRequiredField("clinicalToolInventoryService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
