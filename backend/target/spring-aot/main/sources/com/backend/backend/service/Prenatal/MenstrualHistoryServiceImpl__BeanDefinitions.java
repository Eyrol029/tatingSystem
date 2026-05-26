package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link MenstrualHistoryServiceImpl}.
 */
@Generated
public class MenstrualHistoryServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'menstrualHistoryServiceImpl'.
   */
  public static BeanDefinition getMenstrualHistoryServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MenstrualHistoryServiceImpl.class);
    InstanceSupplier<MenstrualHistoryServiceImpl> instanceSupplier = InstanceSupplier.using(MenstrualHistoryServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(MenstrualHistoryServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
