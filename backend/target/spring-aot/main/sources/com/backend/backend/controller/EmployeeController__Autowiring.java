package com.backend.backend.controller;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link EmployeeController}.
 */
@Generated
public class EmployeeController__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static EmployeeController apply(RegisteredBean registeredBean,
      EmployeeController instance) {
    AutowiredFieldValueResolver.forRequiredField("employeeService").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
