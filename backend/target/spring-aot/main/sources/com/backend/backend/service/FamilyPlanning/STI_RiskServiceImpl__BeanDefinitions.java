package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link STI_RiskServiceImpl}.
 */
@Generated
public class STI_RiskServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'sTI_RiskServiceImpl'.
   */
  public static BeanDefinition getSTIRiskServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(STI_RiskServiceImpl.class);
    InstanceSupplier<STI_RiskServiceImpl> instanceSupplier = InstanceSupplier.using(STI_RiskServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(STI_RiskServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
