package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link MedicalHistoryDetailController}.
 */
@Generated
public class MedicalHistoryDetailController__BeanDefinitions {
  /**
   * Get the bean definition for 'medicalHistoryDetailController'.
   */
  public static BeanDefinition getMedicalHistoryDetailControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MedicalHistoryDetailController.class);
    InstanceSupplier<MedicalHistoryDetailController> instanceSupplier = InstanceSupplier.using(MedicalHistoryDetailController::new);
    instanceSupplier = instanceSupplier.andThen(MedicalHistoryDetailController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
