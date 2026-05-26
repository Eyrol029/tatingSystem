package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link PelvicExaminationController}.
 */
@Generated
public class PelvicExaminationController__BeanDefinitions {
  /**
   * Get the bean definition for 'pelvicExaminationController'.
   */
  public static BeanDefinition getPelvicExaminationControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(PelvicExaminationController.class);
    InstanceSupplier<PelvicExaminationController> instanceSupplier = InstanceSupplier.using(PelvicExaminationController::new);
    instanceSupplier = instanceSupplier.andThen(PelvicExaminationController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
