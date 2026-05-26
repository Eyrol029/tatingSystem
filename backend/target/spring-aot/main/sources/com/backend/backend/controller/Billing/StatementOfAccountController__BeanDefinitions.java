package com.backend.backend.controller.Billing;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link StatementOfAccountController}.
 */
@Generated
public class StatementOfAccountController__BeanDefinitions {
  /**
   * Get the bean definition for 'statementOfAccountController'.
   */
  public static BeanDefinition getStatementOfAccountControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(StatementOfAccountController.class);
    InstanceSupplier<StatementOfAccountController> instanceSupplier = InstanceSupplier.using(StatementOfAccountController::new);
    instanceSupplier = instanceSupplier.andThen(StatementOfAccountController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
