package org.springframework.boot.actuate.autoconfigure.web.mappings;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.actuate.web.mappings.MappingsEndpoint;
import org.springframework.context.ApplicationContext;

/**
 * Bean definitions for {@link MappingsEndpointAutoConfiguration}.
 */
@Generated
public class MappingsEndpointAutoConfiguration__BeanDefinitions {
  /**
   * Get the bean definition for 'mappingsEndpointAutoConfiguration'.
   */
  public static BeanDefinition getMappingsEndpointAutoConfigurationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MappingsEndpointAutoConfiguration.class);
    beanDefinition.setInstanceSupplier(MappingsEndpointAutoConfiguration::new);
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'mappingsEndpoint'.
   */
  private static BeanInstanceSupplier<MappingsEndpoint> getMappingsEndpointInstanceSupplier() {
    return BeanInstanceSupplier.<MappingsEndpoint>forFactoryMethod(MappingsEndpointAutoConfiguration.class, "mappingsEndpoint", ApplicationContext.class, ObjectProvider.class)
            .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.actuate.autoconfigure.web.mappings.MappingsEndpointAutoConfiguration", MappingsEndpointAutoConfiguration.class).mappingsEndpoint(args.get(0), args.get(1)));
  }

  /**
   * Get the bean definition for 'mappingsEndpoint'.
   */
  public static BeanDefinition getMappingsEndpointBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MappingsEndpoint.class);
    beanDefinition.setFactoryBeanName("org.springframework.boot.actuate.autoconfigure.web.mappings.MappingsEndpointAutoConfiguration");
    beanDefinition.setInstanceSupplier(getMappingsEndpointInstanceSupplier());
    return beanDefinition;
  }
}
