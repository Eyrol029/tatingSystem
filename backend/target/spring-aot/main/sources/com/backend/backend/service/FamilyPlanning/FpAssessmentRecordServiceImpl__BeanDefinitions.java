package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link FpAssessmentRecordServiceImpl}.
 */
@Generated
public class FpAssessmentRecordServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'fpAssessmentRecordServiceImpl'.
   */
  public static BeanDefinition getFpAssessmentRecordServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(FpAssessmentRecordServiceImpl.class);
    InstanceSupplier<FpAssessmentRecordServiceImpl> instanceSupplier = InstanceSupplier.using(FpAssessmentRecordServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(FpAssessmentRecordServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
