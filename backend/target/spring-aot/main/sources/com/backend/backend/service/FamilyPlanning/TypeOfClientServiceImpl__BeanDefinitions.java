package com.backend.backend.service.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link TypeOfClientServiceImpl}.
 */
@Generated
public class TypeOfClientServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'typeOfClientServiceImpl'.
   */
  public static BeanDefinition getTypeOfClientServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(TypeOfClientServiceImpl.class);
    InstanceSupplier<TypeOfClientServiceImpl> instanceSupplier = InstanceSupplier.using(TypeOfClientServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(TypeOfClientServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
