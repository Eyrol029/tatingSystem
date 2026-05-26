package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link MedicalHistoryController}.
 */
@Generated
public class MedicalHistoryController__BeanDefinitions {
  /**
   * Get the bean definition for 'medicalHistoryController'.
   */
  public static BeanDefinition getMedicalHistoryControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MedicalHistoryController.class);
    InstanceSupplier<MedicalHistoryController> instanceSupplier = InstanceSupplier.using(MedicalHistoryController::new);
    instanceSupplier = instanceSupplier.andThen(MedicalHistoryController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
