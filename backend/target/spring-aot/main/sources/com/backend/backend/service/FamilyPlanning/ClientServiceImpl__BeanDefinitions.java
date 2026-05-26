package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ClientServiceImpl}.
 */
@Generated
public class ClientServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'clientServiceImpl'.
   */
  public static BeanDefinition getClientServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ClientServiceImpl.class);
    InstanceSupplier<ClientServiceImpl> instanceSupplier = InstanceSupplier.using(ClientServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(ClientServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
