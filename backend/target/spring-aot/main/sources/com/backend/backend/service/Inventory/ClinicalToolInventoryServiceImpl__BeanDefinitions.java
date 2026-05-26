package com.backend.backend.service.Inventory;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ClinicalToolInventoryServiceImpl}.
 */
@Generated
public class ClinicalToolInventoryServiceImpl__BeanDefinitions {
  /**
   * Get the bean definition for 'clinicalToolInventoryServiceImpl'.
   */
  public static BeanDefinition getClinicalToolInventoryServiceImplBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ClinicalToolInventoryServiceImpl.class);
    InstanceSupplier<ClinicalToolInventoryServiceImpl> instanceSupplier = InstanceSupplier.using(ClinicalToolInventoryServiceImpl::new);
    instanceSupplier = instanceSupplier.andThen(ClinicalToolInventoryServiceImpl__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
