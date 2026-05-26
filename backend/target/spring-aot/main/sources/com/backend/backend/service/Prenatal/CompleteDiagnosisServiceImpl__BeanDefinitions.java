package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link CompleteDiagnosisServiceImpl}.
 */
@Generated
public class CompleteDiagnosisServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'completeDiagnosisServiceImpl'.
   */
  public static BeanDefinition getCompleteDiagnosisServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(CompleteDiagnosisServiceImpl.class);
    InstanceSupplier<CompleteDiagnosisServiceImpl> instanceSupplier = InstanceSupplier.using(CompleteDiagnosisServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(CompleteDiagnosisServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
