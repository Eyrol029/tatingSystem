package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link MenstrualHistoryController}.
 */
@Generated
public class MenstrualHistoryController__BeanDefinitions {
  /**
   * Get the bean definition for 'menstrualHistoryController'.
   */
  public static BeanDefinition getMenstrualHistoryControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MenstrualHistoryController.class);
    InstanceSupplier<MenstrualHistoryController> instanceSupplier = InstanceSupplier.using(MenstrualHistoryController::new);
    instanceSupplier = instanceSupplier.andThen(MenstrualHistoryController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
