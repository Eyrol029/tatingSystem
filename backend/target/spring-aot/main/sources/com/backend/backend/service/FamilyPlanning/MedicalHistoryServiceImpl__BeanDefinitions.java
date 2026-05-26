package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link MedicalHistoryServiceImpl}.
 */
@Generated
public class MedicalHistoryServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'medicalHistoryServiceImpl'.
   */
  public static BeanDefinition getMedicalHistoryServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MedicalHistoryServiceImpl.class);
    InstanceSupplier<MedicalHistoryServiceImpl> instanceSupplier = InstanceSupplier.using(MedicalHistoryServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(MedicalHistoryServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
