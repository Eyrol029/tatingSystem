package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ClinicalHistoryAndPhysicalExaminationServiceImpl}.
 */
@Generated
public class ClinicalHistoryAndPhysicalExaminationServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'clinicalHistoryAndPhysicalExaminationServiceImpl'.
   */
  public static BeanDefinition getClinicalHistoryAndPhysicalExaminationServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ClinicalHistoryAndPhysicalExaminationServiceImpl.class);
    InstanceSupplier<ClinicalHistoryAndPhysicalExaminationServiceImpl> instanceSupplier = InstanceSupplier.using(ClinicalHistoryAndPhysicalExaminationServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(ClinicalHistoryAndPhysicalExaminationServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
