package org.springframework.boot.jpa.autoconfigure;

import java.lang.String;
import java.util.List;
import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.aot.BeanInstanceSupplier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Bean definitions for {@link JpaBaseConfiguration}.
 */
@Generated
public class JpaBaseConfiguration__BeanDefinitions {
  /**
   * Bean definitions for {@link JpaBaseConfiguration.JpaWebConfiguration}.
   */
  @Generated
  public static class JpaWebConfiguration {
    /**
     * Get the bean instance supplier for 'org.springframework.boot.jpa.autoconfigure.JpaBaseConfiguration$JpaWebConfiguration'.
     */
    private static BeanInstanceSupplier<JpaBaseConfiguration.JpaWebConfiguration> getJpaWebConfigurationInstanceSupplier(
        ) {
      return BeanInstanceSupplier.<JpaBaseConfiguration.JpaWebConfiguration>forConstructor(JpaProperties.class)
              .withGenerator((registeredBean, args) -> new JpaBaseConfiguration.JpaWebConfiguration(args.get(0)));
    }

    /**
     * Get the bean definition for 'jpaWebConfiguration'.
     */
    public static BeanDefinition getJpaWebConfigurationBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(JpaBaseConfiguration.JpaWebConfiguration.class);
      beanDefinition.setInstanceSupplier(getJpaWebConfigurationInstanceSupplier());
      return beanDefinition;
    }

    /**
     * Get the bean instance supplier for 'openEntityManagerInViewInterceptor'.
     */
    private static BeanInstanceSupplier<OpenEntityManagerInViewInterceptor> getOpenEntityManagerInViewInterceptorInstanceSupplier(
        ) {
      return BeanInstanceSupplier.<OpenEntityManagerInViewInterceptor>forFactoryMethod(JpaBaseConfiguration.JpaWebConfiguration.class, "openEntityManagerInViewInterceptor")
              .withGenerator((registeredBean) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.jpa.autoconfigure.JpaBaseConfiguration$JpaWebConfiguration", JpaBaseConfiguration.JpaWebConfiguration.class).openEntityManagerInViewInterceptor());
    }

    /**
     * Get the bean definition for 'openEntityManagerInViewInterceptor'.
     */
    public static BeanDefinition getOpenEntityManagerInViewInterceptorBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(OpenEntityManagerInViewInterceptor.class);
      beanDefinition.setFactoryBeanName("org.springframework.boot.jpa.autoconfigure.JpaBaseConfiguration$JpaWebConfiguration");
      beanDefinition.setInstanceSupplier(getOpenEntityManagerInViewInterceptorInstanceSupplier());
      return beanDefinition;
    }

    /**
     * Get the bean instance supplier for 'openEntityManagerInViewInterceptorConfigurer'.
     */
    private static BeanInstanceSupplier<WebMvcConfigurer> getOpenEntityManagerInViewInterceptorConfigurerInstanceSupplier(
        ) {
      return BeanInstanceSupplier.<WebMvcConfigurer>forFactoryMethod(JpaBaseConfiguration.JpaWebConfiguration.class, "openEntityManagerInViewInterceptorConfigurer", OpenEntityManagerInViewInterceptor.class)
              .withGenerator((registeredBean, args) -> registeredBean.getBeanFactory().getBean("org.springframework.boot.jpa.autoconfigure.JpaBaseConfiguration$JpaWebConfiguration", JpaBaseConfiguration.JpaWebConfiguration.class).openEntityManagerInViewInterceptorConfigurer(args.get(0)));
    }

    /**
     * Get the bean definition for 'openEntityManagerInViewInterceptorConfigurer'.
     */
    public static BeanDefinition getOpenEntityManagerInViewInterceptorConfigurerBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(WebMvcConfigurer.class);
      beanDefinition.setFactoryBeanName("org.springframework.boot.jpa.autoconfigure.JpaBaseConfiguration$JpaWebConfiguration");
      beanDefinition.setInstanceSupplier(getOpenEntityManagerInViewInterceptorConfigurerInstanceSupplier());
      return beanDefinition;
    }
  }

  /**
   * Bean definitions for {@link JpaBaseConfiguration.PersistenceManagedTypesConfiguration}.
   */
  @Generated
  public static class PersistenceManagedTypesConfiguration {
    /**
     * Get the bean definition for 'persistenceManagedTypesConfiguration'.
     */
    public static BeanDefinition getPersistenceManagedTypesConfigurationBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(JpaBaseConfiguration.PersistenceManagedTypesConfiguration.class);
      beanDefinition.setInstanceSupplier(JpaBaseConfiguration.PersistenceManagedTypesConfiguration::new);
      return beanDefinition;
    }

    /**
     * Get the bean instance for 'persistenceManagedTypes'.
     */
    private static PersistenceManagedTypes getPersistenceManagedTypesInstance() {
      List<String> managedClassNames = List.of("com.backend.backend.model.Appointment.Appointment", "com.backend.backend.model.Billing.Billing", "com.backend.backend.model.Billing.Expense", "com.backend.backend.model.Billing.StatementOfAccount", "com.backend.backend.model.Employee", "com.backend.backend.model.FamilyPlanning.Abdomen", "com.backend.backend.model.FamilyPlanning.Breast", "com.backend.backend.model.FamilyPlanning.Client", "com.backend.backend.model.FamilyPlanning.Conjunctiva", "com.backend.backend.model.FamilyPlanning.Extremities", "com.backend.backend.model.FamilyPlanning.FamilyPlanningRecord", "com.backend.backend.model.FamilyPlanning.FpAssessmentRecord", "com.backend.backend.model.FamilyPlanning.MedicalCondition", "com.backend.backend.model.FamilyPlanning.MedicalHistory", "com.backend.backend.model.FamilyPlanning.MedicalHistoryDetail", "com.backend.backend.model.FamilyPlanning.MethodCurrentlyUsed", "com.backend.backend.model.FamilyPlanning.Neck", "com.backend.backend.model.FamilyPlanning.ObstetricalCondition", "com.backend.backend.model.FamilyPlanning.ObstetricalConditionDetail", "com.backend.backend.model.FamilyPlanning.ObstetricalHistory", "com.backend.backend.model.FamilyPlanning.PelvicExamination", "com.backend.backend.model.FamilyPlanning.PhysicalExamination", "com.backend.backend.model.FamilyPlanning.PregnancyExclusionChecklist", "com.backend.backend.model.FamilyPlanning.RiskForVAW", "com.backend.backend.model.FamilyPlanning.Skin", "com.backend.backend.model.FamilyPlanning.Spouse", "com.backend.backend.model.FamilyPlanning.STI_Condition", "com.backend.backend.model.FamilyPlanning.STI_Risk", "com.backend.backend.model.FamilyPlanning.STI_RiskDetail", "com.backend.backend.model.FamilyPlanning.TypeOfClient", "com.backend.backend.model.Inventory.ClinicalToolInventory", "com.backend.backend.model.Patient", "com.backend.backend.model.PatientService", "com.backend.backend.model.Prenatal.AscertainPregnancyLowRisk", "com.backend.backend.model.Prenatal.ClinicalHistoryAndPhysicalExamination", "com.backend.backend.model.Prenatal.CompleteDiagnosis", "com.backend.backend.model.Prenatal.ConsultationRecord", "com.backend.backend.model.Prenatal.ConsultationVisits", "com.backend.backend.model.Prenatal.LaboratoryResults", "com.backend.backend.model.Prenatal.MedicalSurgicalFactorMeasurements", "com.backend.backend.model.Prenatal.MedicalSurgicalFactorMedConditions", "com.backend.backend.model.Prenatal.MedicalSurgicalFactors", "com.backend.backend.model.Prenatal.MenstrualHistory", "com.backend.backend.model.Prenatal.NormalVitalSign", "com.backend.backend.model.Prenatal.ObstetricRiskFactor", "com.backend.backend.model.Prenatal.PrenatalRecord", "com.backend.backend.model.Prenatal.TreatmentManagement", "com.backend.backend.model.Prenatal.VitalSigns", "com.backend.backend.model.Ultrasound.UltrasoundRecord", "com.backend.backend.model.User");
      List<String> managedPackages = List.of();
      return PersistenceManagedTypes.of(managedClassNames, managedPackages);
    }

    /**
     * Get the bean definition for 'persistenceManagedTypes'.
     */
    public static BeanDefinition getPersistenceManagedTypesBeanDefinition() {
      RootBeanDefinition beanDefinition = new RootBeanDefinition(JpaBaseConfiguration.PersistenceManagedTypesConfiguration.class);
      beanDefinition.setTargetType(PersistenceManagedTypes.class);
      beanDefinition.setPrimary(true);
      beanDefinition.setInstanceSupplier(PersistenceManagedTypesConfiguration::getPersistenceManagedTypesInstance);
      return beanDefinition;
    }
  }
}
