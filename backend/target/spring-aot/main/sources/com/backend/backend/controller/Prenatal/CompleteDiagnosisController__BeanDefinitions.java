package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link CompleteDiagnosisController}.
 */
@Generated
public class CompleteDiagnosisController__BeanDefinitions {
  /**
   * Get the bean definition for 'completeDiagnosisController'.
   */
  public static BeanDefinition getCompleteDiagnosisControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(CompleteDiagnosisController.class);
    InstanceSupplier<CompleteDiagnosisController> instanceSupplier = InstanceSupplier.using(CompleteDiagnosisController::new);
    instanceSupplier = instanceSupplier.andThen(CompleteDiagnosisController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
