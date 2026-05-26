package com.backend.backend.service.Prenatal;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link NormalVitalSignServiceImpl}.
 */
@Generated
public class NormalVitalSignServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'normalVitalSignServiceImpl'.
   */
  public static BeanDefinition getNormalVitalSignServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(NormalVitalSignServiceImpl.class);
    InstanceSupplier<NormalVitalSignServiceImpl> instanceSupplier = InstanceSupplier.using(NormalVitalSignServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(NormalVitalSignServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
