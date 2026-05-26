package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link TreatmentManagementController}.
 */
@Generated
public class TreatmentManagementController__BeanDefinitions {
  /**
   * Get the bean definition for 'treatmentManagementController'.
   */
  public static BeanDefinition getTreatmentManagementControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(TreatmentManagementController.class);
    InstanceSupplier<TreatmentManagementController> instanceSupplier = InstanceSupplier.using(TreatmentManagementController::new);
    instanceSupplier = instanceSupplier.andThen(TreatmentManagementController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
