package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link AscertainPregnancyLowRiskServiceImpl}.
 */
@Generated
public class AscertainPregnancyLowRiskServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'ascertainPregnancyLowRiskServiceImpl'.
   */
  public static BeanDefinition getAscertainPregnancyLowRiskServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(AscertainPregnancyLowRiskServiceImpl.class);
    InstanceSupplier<AscertainPregnancyLowRiskServiceImpl> instanceSupplier = InstanceSupplier.using(AscertainPregnancyLowRiskServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(AscertainPregnancyLowRiskServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
