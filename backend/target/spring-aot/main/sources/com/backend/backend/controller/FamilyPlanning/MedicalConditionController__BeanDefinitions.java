package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link MedicalConditionController}.
 */
@Generated
public class MedicalConditionController__BeanDefinitions {
  /**
   * Get the bean definition for 'medicalConditionController'.
   */
  public static BeanDefinition getMedicalConditionControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MedicalConditionController.class);
    InstanceSupplier<MedicalConditionController> instanceSupplier = InstanceSupplier.using(MedicalConditionController::new);
    instanceSupplier = instanceSupplier.andThen(MedicalConditionController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
