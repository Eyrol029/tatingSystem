package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ConjunctivaController}.
 */
@Generated
public class ConjunctivaController__BeanDefinitions {
  /**
   * Get the bean definition for 'conjunctivaController'.
   */
  public static BeanDefinition getConjunctivaControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ConjunctivaController.class);
    InstanceSupplier<ConjunctivaController> instanceSupplier = InstanceSupplier.using(ConjunctivaController::new);
    instanceSupplier = instanceSupplier.andThen(ConjunctivaController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
