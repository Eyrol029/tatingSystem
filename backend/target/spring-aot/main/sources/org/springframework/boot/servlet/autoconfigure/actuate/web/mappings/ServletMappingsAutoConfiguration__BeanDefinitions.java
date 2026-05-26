package org.springframework.boot.servlet.autoconfigure.actuate.web.mappings;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.servlet.actuate.web.mappings.FiltersMappingDescriptionProvider;
import org.springframework.boot.servlet.actuate.web.mappings.ServletsMappingDescriptionProvider;

/**
 * Bean definitions for {@link ServletMappingsAutoConfiguration}.
 */
@Generated
public class ServletMappingsAutoConfiguration__BeanDefinitions {
  /**
   * Get the bean definition for 'servletMappingsAutoConfiguration'.
   */
  public static BeanDefinition getServletMappingsAutoConfigurationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ServletMappingsAutoConfiguration.class);
    beanDefinition.setInstanceSupplier(ServletMappingsAutoConfiguration::new);
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'servletMappingDescriptionProvider'.
   */
  private static BeanInstanceSupplier<ServletsMappingDescriptionProvider> getServletMappingDescriptionProviderInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<ServletsMappingDescriptionProvider>forFactoryMethod(ServletMappingsAutoConfiguration.class, "servletMappingDescriptionProvider")
            .withGenerator((registeredBean) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.servlet.autoconfigure.actuate.web.mappings.ServletMappingsAutoConfiguration", ServletMappingsAutoConfiguration.class).servletMappingDescriptionProvider());
  }

  /**
   * Get the bean definition for 'servletMappingDescriptionProvider'.
   */
  public static BeanDefinition getServletMappingDescriptionProviderBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(ServletsMappingDescriptionProvider.class);
    beanDefinition.setFactoryBeanName("org.springframework.boot.servlet.autoconfigure.actuate.web.mappings.ServletMappingsAutoConfiguration");
    beanDefinition.setInstanceSupplier(getServletMappingDescriptionProviderInstanceSupplier());
    return beanDefinition;
  }

  /**
   * Get the bean instance supplier for 'filterMappingDescriptionProvider'.
   */
  private static BeanInstanceSupplier<FiltersMappingDescriptionProvider> getFilterMappingDescriptionProviderInstanceSupplier(
      ) {
    return BeanInstanceSupplier.<FiltersMappingDescriptionProvider>forFactoryMethod(ServletMappingsAutoConfiguration.class, "filterMappingDescriptionProvider")
            .withGenerator((registeredBean) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.servlet.autoconfigure.actuate.web.mappings.ServletMappingsAutoConfiguration", ServletMappingsAutoConfiguration.class).filterMappingDescriptionProvider());
  }

  /**
   * Get the bean definition for 'filterMappingDescriptionProvider'.
   */
  public static BeanDefinition getFilterMappingDescriptionProviderBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(FiltersMappingDescriptionProvider.class);
    beanDefinition.setFactoryBeanName("org.springframework.boot.servlet.autoconfigure.actuate.web.mappings.ServletMappingsAutoConfiguration");
    beanDefinition.setInstanceSupplier(getFilterMappingDescriptionProviderInstanceSupplier());
    return beanDefinition;
  }
}
