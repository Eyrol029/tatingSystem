package com.backend.backend.service.Billing;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link BillingServiceImpl}.
 */
@Generated
public class BillingServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'billingServiceImpl'.
   */
  public static BeanDefinition getBillingServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(BillingServiceImpl.class);
    InstanceSupplier<BillingServiceImpl> instanceSupplier = InstanceSupplier.using(BillingServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(BillingServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
