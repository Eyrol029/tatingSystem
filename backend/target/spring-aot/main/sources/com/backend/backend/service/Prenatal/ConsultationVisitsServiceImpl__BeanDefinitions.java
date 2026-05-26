package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ConsultationVisitsServiceImpl}.
 */
@Generated
public class ConsultationVisitsServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'consultationVisitsServiceImpl'.
   */
  public static BeanDefinition getConsultationVisitsServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ConsultationVisitsServiceImpl.class);
    InstanceSupplier<ConsultationVisitsServiceImpl> instanceSupplier = InstanceSupplier.using(ConsultationVisitsServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(ConsultationVisitsServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
