package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link PhysicalExaminationController}.
 */
@Generated
public class PhysicalExaminationController__BeanDefinitions {
  /**
   * Get the bean definition for 'physicalExaminationController'.
   */
  public static BeanDefinition getPhysicalExaminationControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(PhysicalExaminationController.class);
    InstanceSupplier<PhysicalExaminationController> instanceSupplier = InstanceSupplier.using(PhysicalExaminationController::new);
    instanceSupplier = instanceSupplier.andThen(PhysicalExaminationController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
