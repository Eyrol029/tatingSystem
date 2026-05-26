package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link STI_RiskDetailServiceImpl}.
 */
@Generated
public class STI_RiskDetailServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'sTI_RiskDetailServiceImpl'.
   */
  public static BeanDefinition getSTIRiskDetailServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(STI_RiskDetailServiceImpl.class);
    InstanceSupplier<STI_RiskDetailServiceImpl> instanceSupplier = InstanceSupplier.using(STI_RiskDetailServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(STI_RiskDetailServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
