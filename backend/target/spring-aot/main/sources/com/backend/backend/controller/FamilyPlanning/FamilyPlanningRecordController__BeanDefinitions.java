package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link FamilyPlanningRecordController}.
 */
@Generated
public class FamilyPlanningRecordController__BeanDefinitions {
  /**
   * Get the bean definition for 'familyPlanningRecordController'.
   */
  public static BeanDefinition getFamilyPlanningRecordControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(FamilyPlanningRecordController.class);
    InstanceSupplier<FamilyPlanningRecordController> instanceSupplier = InstanceSupplier.using(FamilyPlanningRecordController::new);
    instanceSupplier = instanceSupplier.andThen(FamilyPlanningRecordController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
