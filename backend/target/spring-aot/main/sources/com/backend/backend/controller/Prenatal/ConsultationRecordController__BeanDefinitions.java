package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ConsultationRecordController}.
 */
@Generated
public class ConsultationRecordController__BeanDefinitions {
  /**
   * Get the bean definition for 'consultationRecordController'.
   */
  public static BeanDefinition getConsultationRecordControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ConsultationRecordController.class);
    InstanceSupplier<ConsultationRecordController> instanceSupplier = InstanceSupplier.using(ConsultationRecordController::new);
    instanceSupplier = instanceSupplier.andThen(ConsultationRecordController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
