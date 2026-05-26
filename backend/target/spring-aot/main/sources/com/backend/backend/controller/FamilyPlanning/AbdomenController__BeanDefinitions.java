package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link AbdomenController}.
 */
@Generated
public class AbdomenController__BeanDefinitions {
  /**
   * Get the bean definition for 'abdomenController'.
   */
  public static BeanDefinition getAbdomenControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(AbdomenController.class);
    InstanceSupplier<AbdomenController> instanceSupplier = InstanceSupplier.using(AbdomenController::new);
    instanceSupplier = instanceSupplier.andThen(AbdomenController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
