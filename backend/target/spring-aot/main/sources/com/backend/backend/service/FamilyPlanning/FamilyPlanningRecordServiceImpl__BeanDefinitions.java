package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link FamilyPlanningRecordServiceImpl}.
 */
@Generated
public class FamilyPlanningRecordServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'familyPlanningRecordServiceImpl'.
   */
  public static BeanDefinition getFamilyPlanningRecordServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(FamilyPlanningRecordServiceImpl.class);
    InstanceSupplier<FamilyPlanningRecordServiceImpl> instanceSupplier = InstanceSupplier.using(FamilyPlanningRecordServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(FamilyPlanningRecordServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
