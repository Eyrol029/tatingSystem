package com.backend.backend.controller.Inventory;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ClinicalToolInventoryController}.
 */
@Generated
public class ClinicalToolInventoryController__BeanDefinitions {
  /**
   * Get the bean definition for 'clinicalToolInventoryController'.
   */
  public static BeanDefinition getClinicalToolInventoryControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ClinicalToolInventoryController.class);
    InstanceSupplier<ClinicalToolInventoryController> instanceSupplier = InstanceSupplier.using(ClinicalToolInventoryController::new);
    instanceSupplier = instanceSupplier.andThen(ClinicalToolInventoryController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
