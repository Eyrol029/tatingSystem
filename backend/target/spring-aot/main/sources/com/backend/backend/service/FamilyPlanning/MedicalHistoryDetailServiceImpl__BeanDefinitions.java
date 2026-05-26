package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link MedicalHistoryDetailServiceImpl}.
 */
@Generated
public class MedicalHistoryDetailServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'medicalHistoryDetailServiceImpl'.
   */
  public static BeanDefinition getMedicalHistoryDetailServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MedicalHistoryDetailServiceImpl.class);
    InstanceSupplier<MedicalHistoryDetailServiceImpl> instanceSupplier = InstanceSupplier.using(MedicalHistoryDetailServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(MedicalHistoryDetailServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
