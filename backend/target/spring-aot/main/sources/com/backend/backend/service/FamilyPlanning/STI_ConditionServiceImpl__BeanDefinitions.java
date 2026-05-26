package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link STI_ConditionServiceImpl}.
 */
@Generated
public class STI_ConditionServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'sTI_ConditionServiceImpl'.
   */
  public static BeanDefinition getSTIConditionServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(STI_ConditionServiceImpl.class);
    InstanceSupplier<STI_ConditionServiceImpl> instanceSupplier = InstanceSupplier.using(STI_ConditionServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(STI_ConditionServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
