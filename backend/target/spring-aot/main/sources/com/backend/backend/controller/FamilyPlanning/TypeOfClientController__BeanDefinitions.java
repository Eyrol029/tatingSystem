package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link TypeOfClientController}.
 */
@Generated
public class TypeOfClientController__BeanDefinitions {
  /**
   * Get the bean definition for 'typeOfClientController'.
   */
  public static BeanDefinition getTypeOfClientControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(TypeOfClientController.class);
    InstanceSupplier<TypeOfClientController> instanceSupplier = InstanceSupplier.using(TypeOfClientController::new);
    instanceSupplier = instanceSupplier.andThen(TypeOfClientController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
