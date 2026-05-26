package com.backend.backend.service.Billing;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link StatementOfAccountServiceImpl}.
 */
@Generated
public class StatementOfAccountServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'statementOfAccountServiceImpl'.
   */
  public static BeanDefinition getStatementOfAccountServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(StatementOfAccountServiceImpl.class);
    InstanceSupplier<StatementOfAccountServiceImpl> instanceSupplier = InstanceSupplier.using(StatementOfAccountServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(StatementOfAccountServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
