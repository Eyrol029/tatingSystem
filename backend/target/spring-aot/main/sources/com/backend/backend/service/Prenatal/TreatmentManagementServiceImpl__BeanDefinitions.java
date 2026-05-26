package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link TreatmentManagementServiceImpl}.
 */
@Generated
public class TreatmentManagementServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'treatmentManagementServiceImpl'.
   */
  public static BeanDefinition getTreatmentManagementServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(TreatmentManagementServiceImpl.class);
    InstanceSupplier<TreatmentManagementServiceImpl> instanceSupplier = InstanceSupplier.using(TreatmentManagementServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(TreatmentManagementServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
