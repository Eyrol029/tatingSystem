package com.backend.backend.service.Appointment;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link AppointmentServiceImpl}.
 */
@Generated
public class AppointmentServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'appointmentServiceImpl'.
   */
  public static BeanDefinition getAppointmentServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(AppointmentServiceImpl.class);
    InstanceSupplier<AppointmentServiceImpl> instanceSupplier = InstanceSupplier.using(AppointmentServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(AppointmentServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
