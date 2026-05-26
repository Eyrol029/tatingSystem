package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link MedicalSurgicalFactorsServiceImpl}.
 */
@Generated
public class MedicalSurgicalFactorsServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'medicalSurgicalFactorsServiceImpl'.
   */
  public static BeanDefinition getMedicalSurgicalFactorsServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MedicalSurgicalFactorsServiceImpl.class);
    InstanceSupplier<MedicalSurgicalFactorsServiceImpl> instanceSupplier = InstanceSupplier.using(MedicalSurgicalFactorsServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(MedicalSurgicalFactorsServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
