package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ClinicalHistoryAndPhysicalExaminationController}.
 */
@Generated
public class ClinicalHistoryAndPhysicalExaminationController__BeanDefinitions {
  /**
   * Get the bean definition for 'clinicalHistoryAndPhysicalExaminationController'.
   */
  public static BeanDefinition getClinicalHistoryAndPhysicalExaminationControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ClinicalHistoryAndPhysicalExaminationController.class);
    InstanceSupplier<ClinicalHistoryAndPhysicalExaminationController> instanceSupplier = InstanceSupplier.using(ClinicalHistoryAndPhysicalExaminationController::new);
    instanceSupplier = instanceSupplier.andThen(ClinicalHistoryAndPhysicalExaminationController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
