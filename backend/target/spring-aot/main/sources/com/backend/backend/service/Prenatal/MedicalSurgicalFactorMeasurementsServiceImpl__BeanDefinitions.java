package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link MedicalSurgicalFactorMeasurementsServiceImpl}.
 */
@Generated
public class MedicalSurgicalFactorMeasurementsServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'medicalSurgicalFactorMeasurementsServiceImpl'.
   */
  public static BeanDefinition getMedicalSurgicalFactorMeasurementsServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MedicalSurgicalFactorMeasurementsServiceImpl.class);
    InstanceSupplier<MedicalSurgicalFactorMeasurementsServiceImpl> instanceSupplier = InstanceSupplier.using(MedicalSurgicalFactorMeasurementsServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(MedicalSurgicalFactorMeasurementsServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
