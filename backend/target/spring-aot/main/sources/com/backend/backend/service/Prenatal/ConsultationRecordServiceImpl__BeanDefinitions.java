package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ConsultationRecordServiceImpl}.
 */
@Generated
public class ConsultationRecordServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'consultationRecordServiceImpl'.
   */
  public static BeanDefinition getConsultationRecordServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ConsultationRecordServiceImpl.class);
    InstanceSupplier<ConsultationRecordServiceImpl> instanceSupplier = InstanceSupplier.using(ConsultationRecordServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(ConsultationRecordServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
