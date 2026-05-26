package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ObstetricRiskFactorServiceImpl}.
 */
@Generated
public class ObstetricRiskFactorServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'obstetricRiskFactorServiceImpl'.
   */
  public static BeanDefinition getObstetricRiskFactorServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ObstetricRiskFactorServiceImpl.class);
    InstanceSupplier<ObstetricRiskFactorServiceImpl> instanceSupplier = InstanceSupplier.using(ObstetricRiskFactorServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(ObstetricRiskFactorServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
