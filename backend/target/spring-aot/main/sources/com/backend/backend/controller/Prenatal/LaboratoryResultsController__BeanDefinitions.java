package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link LaboratoryResultsController}.
 */
@Generated
public class LaboratoryResultsController__BeanDefinitions {
  /**
   * Get the bean definition for 'laboratoryResultsController'.
   */
  public static BeanDefinition getLaboratoryResultsControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(LaboratoryResultsController.class);
    InstanceSupplier<LaboratoryResultsController> instanceSupplier = InstanceSupplier.using(LaboratoryResultsController::new);
    instanceSupplier = instanceSupplier.andThen(LaboratoryResultsController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
