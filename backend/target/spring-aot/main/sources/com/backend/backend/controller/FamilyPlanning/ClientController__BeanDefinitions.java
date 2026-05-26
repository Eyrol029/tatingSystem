package com.backend.backend.controller.FamilyPlanning;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.InstanceSupplier;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link ClientController}.
 */
@Generated
public class ClientController__BeanDefinitions {
  /**
   * Get the bean definition for 'clientController'.
   */
  public static BeanDefinition getClientControllerBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ClientController.class);
    InstanceSupplier<ClientController> instanceSupplier = InstanceSupplier.using(ClientController::new);
    instanceSupplier = instanceSupplier.andThen(ClientController__Autowiring::apply);
    beanDefinition.setInstanceSupplier(instanceSupplier);
    return beanDefinition;
  }
}
