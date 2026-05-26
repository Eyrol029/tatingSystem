package com.backend.backend.service.Billing;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link StatementOfAccountServiceImpl}.
 */
@Generated
public class StatementOfAccountServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static StatementOfAccountServiceImpl apply(RegisteredBean registeredBean,
      StatementOfAccountServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
