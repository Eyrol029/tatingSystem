package com.backend.backend.controller.Billing;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link StatementOfAccountController}.
 */
@Generated
public class StatementOfAccountController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static StatementOfAccountController apply(RegisteredBean registeredBean,
      StatementOfAccountController instance) {
    AutowiredFieldValueResolver.forRequiredField("service").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
