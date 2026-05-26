package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link MedicalSurgicalFactorMedConditionsServiceImpl}.
 */
@Generated
public class MedicalSurgicalFactorMedConditionsServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'medicalSurgicalFactorMedConditionsServiceImpl'.
   */
  public static BeanDefinition getMedicalSurgicalFactorMedConditionsServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MedicalSurgicalFactorMedConditionsServiceImpl.class);
    InstanceSupplier<MedicalSurgicalFactorMedConditionsServiceImpl> instanceSupplier = InstanceSupplier.using(MedicalSurgicalFactorMedConditionsServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(MedicalSurgicalFactorMedConditionsServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
