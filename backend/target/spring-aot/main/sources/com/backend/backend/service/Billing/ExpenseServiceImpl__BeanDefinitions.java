package com.backend.backend.service.Billing;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ExpenseServiceImpl}.
 */
@Generated
public class ExpenseServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'expenseServiceImpl'.
   */
  public static BeanDefinition getExpenseServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ExpenseServiceImpl.class);
    InstanceSupplier<ExpenseServiceImpl> instanceSupplier = InstanceSupplier.using(ExpenseServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(ExpenseServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
