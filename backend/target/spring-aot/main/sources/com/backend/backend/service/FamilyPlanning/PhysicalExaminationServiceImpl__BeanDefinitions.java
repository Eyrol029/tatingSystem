package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link PhysicalExaminationServiceImpl}.
 */
@Generated
public class PhysicalExaminationServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'physicalExaminationServiceImpl'.
   */
  public static BeanDefinition getPhysicalExaminationServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(PhysicalExaminationServiceImpl.class);
    InstanceSupplier<PhysicalExaminationServiceImpl> instanceSupplier = InstanceSupplier.using(PhysicalExaminationServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(PhysicalExaminationServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
