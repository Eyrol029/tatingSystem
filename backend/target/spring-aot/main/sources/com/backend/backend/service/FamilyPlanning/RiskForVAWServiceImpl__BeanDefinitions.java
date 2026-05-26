package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link RiskForVAWServiceImpl}.
 */
@Generated
public class RiskForVAWServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'riskForVAWServiceImpl'.
   */
  public static BeanDefinition getRiskForVAWServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(RiskForVAWServiceImpl.class);
    InstanceSupplier<RiskForVAWServiceImpl> instanceSupplier = InstanceSupplier.using(RiskForVAWServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(RiskForVAWServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
