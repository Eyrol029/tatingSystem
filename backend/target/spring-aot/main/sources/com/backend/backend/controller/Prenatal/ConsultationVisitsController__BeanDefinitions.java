package com.backend.backend.controller.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ConsultationVisitsController}.
 */
@Generated
public class ConsultationVisitsController__BeanDefinitions {
  /**
   * Get the bean definition for 'consultationVisitsController'.
   */
  public static BeanDefinition getConsultationVisitsControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ConsultationVisitsController.class);
    InstanceSupplier<ConsultationVisitsController> instanceSupplier = InstanceSupplier.using(ConsultationVisitsController::new);
    instanceSupplier = instanceSupplier.andThen(ConsultationVisitsController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
