package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link PregnancyExclusionChecklistServiceImpl}.
 */
@Generated
public class PregnancyExclusionChecklistServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'pregnancyExclusionChecklistServiceImpl'.
   */
  public static BeanDefinition getPregnancyExclusionChecklistServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(PregnancyExclusionChecklistServiceImpl.class);
    InstanceSupplier<PregnancyExclusionChecklistServiceImpl> instanceSupplier = InstanceSupplier.using(PregnancyExclusionChecklistServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(PregnancyExclusionChecklistServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
