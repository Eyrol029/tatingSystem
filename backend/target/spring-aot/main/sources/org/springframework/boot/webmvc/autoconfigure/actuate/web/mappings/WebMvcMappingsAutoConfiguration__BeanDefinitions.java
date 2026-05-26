package org.springframework.boot.webmvc.autoconfigure.actuate.web.mappings;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.webmvc.actuate.web.mappings.DispatcherServletsMappingDescriptionProvider;

/**
 * Bean definitions for {@link WebMvcMappingsAutoConfiguration}.
 */
@Generated
public class WebMvcMappingsAutoConfiguration__BeanDefinitions {
  /**
   * Get the bean definition for 'webMvcMappingsAutoConfiguration'.
   */
  public static BeanDefinition getWebMvcMappingsAutoConfigurationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(WebMvcMappingsAutoConfiguration.class);
    beanDefinition.setInstanceSupplier(WebMvcMappingsAutoConfiguration::new);
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'dispatcherServletMappingDescriptionProvider'.
   */
  private static BeanInstanceSupplier<DispatcherServletsMappingDescriptionProvider> getDispatcherServletMappingDescriptionProviderInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<DispatcherServletsMappingDescriptionProvider>forFactoryMethod(WebMvcMappingsAutoConfiguration.class, "dispatcherServletMappingDescriptionProvider")
            .withGenerator((registeredBean) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.webmvc.autoconfigure.actuate.web.mappings.WebMvcMappingsAutoConfiguration", WebMvcMappingsAutoConfiguration.class).dispatcherServletMappingDescriptionProvider());
  }

  /**
   * Get the bean definition for 'dispatcherServletMappingDescriptionProvider'.
   */
  public static BeanDefinition getDispatcherServletMappingDescriptionProviderBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(DispatcherServletsMappingDescriptionProvider.class);
    beanDefinition.setFactoryBeanName("org.springframework.boot.webmvc.autoconfigure.actuate.web.mappings.WebMvcMappingsAutoConfiguration");
    beanDefinition.setInstanceSupplier(getDispatcherServletMappingDescriptionProviderInstanceSupplier());
    return beanDefinition;
  }
}
