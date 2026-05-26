package com.backend.backend.service.Appointment;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.AutowiredFieldValueResolver;
import org.springframework.beans.factory.support.RegisteredBean;

/**
 * Autowiring for {@link AppointmentServiceImpl}.
 */
@Generated
public class AppointmentServiceImpl__Autowiring {
  /**
   * Apply the autowiring.
   */
  public static AppointmentServiceImpl apply(RegisteredBean registeredBean,
      AppointmentServiceImpl instance) {
    AutowiredFieldValueResolver.forRequiredField("repository").resolveAndSet(registeredBean, instance);
    return instance;
  }
}
