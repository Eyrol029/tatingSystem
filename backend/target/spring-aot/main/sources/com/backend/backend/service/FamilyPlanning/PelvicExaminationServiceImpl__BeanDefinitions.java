package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link PelvicExaminationServiceImpl}.
 */
@Generated
public class PelvicExaminationServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'pelvicExaminationServiceImpl'.
   */
  public static BeanDefinition getPelvicExaminationServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(PelvicExaminationServiceImpl.class);
    InstanceSupplier<PelvicExaminationServiceImpl> instanceSupplier = InstanceSupplier.using(PelvicExaminationServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(PelvicExaminationServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
