package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ObstetricalConditionServiceImpl}.
 */
@Generated
public class ObstetricalConditionServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'obstetricalConditionServiceImpl'.
   */
  public static BeanDefinition getObstetricalConditionServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ObstetricalConditionServiceImpl.class);
    InstanceSupplier<ObstetricalConditionServiceImpl> instanceSupplier = InstanceSupplier.using(ObstetricalConditionServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(ObstetricalConditionServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
