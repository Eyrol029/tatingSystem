package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link PregnancyExclusionChecklistController}.
 */
@Generated
public class PregnancyExclusionChecklistController__BeanDefinitions {
  /**
   * Get the bean definition for 'pregnancyExclusionChecklistController'.
   */
  public static BeanDefinition getPregnancyExclusionChecklistControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(PregnancyExclusionChecklistController.class);
    InstanceSupplier<PregnancyExclusionChecklistController> instanceSupplier = InstanceSupplier.using(PregnancyExclusionChecklistController::new);
    instanceSupplier = instanceSupplier.andThen(PregnancyExclusionChecklistController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
