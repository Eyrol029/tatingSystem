package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link FpAssessmentRecordController}.
 */
@Generated
public class FpAssessmentRecordController__BeanDefinitions {
  /**
   * Get the bean definition for 'fpAssessmentRecordController'.
   */
  public static BeanDefinition getFpAssessmentRecordControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(FpAssessmentRecordController.class);
    InstanceSupplier<FpAssessmentRecordController> instanceSupplier = InstanceSupplier.using(FpAssessmentRecordController::new);
    instanceSupplier = instanceSupplier.andThen(FpAssessmentRecordController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
