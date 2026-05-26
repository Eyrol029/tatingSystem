package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link MedicalConditionServiceImpl}.
 */
@Generated
public class MedicalConditionServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'medicalConditionServiceImpl'.
   */
  public static BeanDefinition getMedicalConditionServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MedicalConditionServiceImpl.class);
    InstanceSupplier<MedicalConditionServiceImpl> instanceSupplier = InstanceSupplier.using(MedicalConditionServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(MedicalConditionServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
