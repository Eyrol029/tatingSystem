package com.backend.backend.service;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link EmployeeServiceImpl}.
 */
@Generated
public class EmployeeServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static EmployeeServiceImpl apply(RegisteredBean registeredBean,
      EmployeeServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("employeeRepository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
