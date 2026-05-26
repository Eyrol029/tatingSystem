package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ObstetricalHistoryServiceImpl}.
 */
@Generated
public class ObstetricalHistoryServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'obstetricalHistoryServiceImpl'.
   */
  public static BeanDefinition getObstetricalHistoryServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ObstetricalHistoryServiceImpl.class);
    InstanceSupplier<ObstetricalHistoryServiceImpl> instanceSupplier = InstanceSupplier.using(ObstetricalHistoryServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(ObstetricalHistoryServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
