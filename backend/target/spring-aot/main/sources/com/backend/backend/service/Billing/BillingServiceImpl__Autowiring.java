package com.backend.backend.service.Billing;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link BillingServiceImpl}.
 */
@Generated
public class BillingServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static BillingServiceImpl apply(RegisteredBean registeredBean,
      BillingServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
