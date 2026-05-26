package com.backend.backend.service.Billing;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link ExpenseServiceImpl}.
 */
@Generated
public class ExpenseServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static ExpenseServiceImpl apply(RegisteredBean registeredBean,
      ExpenseServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
