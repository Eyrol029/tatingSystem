package com.backend.backend;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Bean definitions for {@link DatabaseMigration}.
 */
@Generated
public class DatabaseMigration__BeanDefinitions {
  /**
   * Get the bean instance supplier for 'databaseMigration'.
   */
  private static BeanInstanceSupplier<DatabaseMigration> getDatabaseMigrationInstanceSupplier() {
    return BeanInstanceSupplier.<DatabaseMigration>forConstructor(JdbcTemplate.class)
            .withGenerator((registeredBean, args) -> new DatabaseMigration(args.get(0)));
  }

  /**
   * Get the bean definition for 'databaseMigration'.
   */
  public static BeanDefinition getDatabaseMigrationBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(DatabaseMigration.class);
    beanDefinition.setInstanceSupplier(getDatabaseMigrationInstanceSupplier());
    return beanDefinition;
  }
}
