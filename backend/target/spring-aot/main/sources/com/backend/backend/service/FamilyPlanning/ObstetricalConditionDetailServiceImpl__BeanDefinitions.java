package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ObstetricalConditionDetailServiceImpl}.
 */
@Generated
public class ObstetricalConditionDetailServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'obstetricalConditionDetailServiceImpl'.
   */
  public static BeanDefinition getObstetricalConditionDetailServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ObstetricalConditionDetailServiceImpl.class);
    InstanceSupplier<ObstetricalConditionDetailServiceImpl> instanceSupplier = InstanceSupplier.using(ObstetricalConditionDetailServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(ObstetricalConditionDetailServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
