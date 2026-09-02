
package com.backend.backend;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseMigration {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseMigration(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void migratePatientServiceTable() {
        try {
            // Drop old integer columns if they still exist
            jdbcTemplate.execute(
                "ALTER TABLE \"PatientService\" DROP COLUMN IF EXISTS \"serviceID\""
            );
            jdbcTemplate.execute(
                "ALTER TABLE \"PatientService\" DROP COLUMN IF EXISTS \"employeeID\""
            );
            jdbcTemplate.execute(
                "ALTER TABLE \"PatientService\" DROP COLUMN IF EXISTS \"wardID\""
            );

            // Add new String columns if they don't already exist
            jdbcTemplate.execute(
                "ALTER TABLE \"PatientService\" ADD COLUMN IF NOT EXISTS \"serviceName\" VARCHAR(255) NOT NULL DEFAULT 'Unknown'"
            );
            jdbcTemplate.execute(
                "ALTER TABLE \"PatientService\" ADD COLUMN IF NOT EXISTS \"employeeName\" VARCHAR(255)"
            );
            jdbcTemplate.execute(
                "ALTER TABLE \"PatientService\" ADD COLUMN IF NOT EXISTS \"wardName\" VARCHAR(255)"
            );
            jdbcTemplate.execute(
                "ALTER TABLE \"PatientService\" ADD COLUMN IF NOT EXISTS \"caseNumber\" VARCHAR(255)"
            );
            jdbcTemplate.execute(
                "ALTER TABLE \"StatementOfAccount\" ADD COLUMN IF NOT EXISTS \"caseNumber\" VARCHAR(255)"
            );
            jdbcTemplate.execute(
                "ALTER TABLE \"Patient\" ADD COLUMN IF NOT EXISTS \"patientCode\" VARCHAR(50)"
            );
            jdbcTemplate.execute(
                "ALTER TABLE \"Patient\" ADD COLUMN IF NOT EXISTS \"numberOfPregnancy\" INT DEFAULT 0"
            );
            jdbcTemplate.execute(
                "UPDATE \"Patient\" SET \"numberOfPregnancy\" = 0 WHERE \"numberOfPregnancy\" IS NULL"
            );
            jdbcTemplate.execute(
                "ALTER TABLE \"Patient\" ALTER COLUMN \"numberOfPregnancy\" SET DEFAULT 0"
            );
            jdbcTemplate.execute(
                "ALTER TABLE \"Patient\" ALTER COLUMN \"numberOfPregnancy\" SET NOT NULL"
            );

            // Newborn Care columns in postpartum_care_records
            String[] newbornCols = {
                "\"immediateDrying\" BOOLEAN", "\"immediate_drying\" BOOLEAN",
                "\"skinToSkinContact\" BOOLEAN", "\"skin_to_skin_contact\" BOOLEAN",
                "\"properCordClamping\" BOOLEAN", "\"proper_cord_clamping\" BOOLEAN",
                "\"earlyBreastfeedingInitiation\" BOOLEAN", "\"early_breastfeeding_initiation\" BOOLEAN",
                "\"eyeProphylaxis\" BOOLEAN", "\"eye_prophylaxis\" BOOLEAN",
                "\"vitaminKAdministered\" BOOLEAN", "\"vitamin_k_administered\" BOOLEAN",
                "\"hepatitisBVaccine\" BOOLEAN", "\"hepatitis_b_vaccine\" BOOLEAN",
                "\"bcgVaccine\" BOOLEAN", "\"bcg_vaccine\" BOOLEAN",
                "\"cordCareApplied\" BOOLEAN", "\"cord_care_applied\" BOOLEAN",
                "\"birthWeight\" VARCHAR(50)", "\"birth_weight\" VARCHAR(50)",
                "\"birthLength\" VARCHAR(50)", "\"birth_length\" VARCHAR(50)",
                "\"headCircumference\" VARCHAR(50)", "\"head_circumference\" VARCHAR(50)",
                "\"chestCircumference\" VARCHAR(50)", "\"chest_circumference\" VARCHAR(50)",
                "\"apgarScore1Min\" VARCHAR(50)", "\"apgar_score1_min\" VARCHAR(50)",
                "\"apgarScore5Min\" VARCHAR(50)", "\"apgar_score5_min\" VARCHAR(50)",
                "\"gestationalAgeWeeks\" VARCHAR(50)", "\"gestational_age_weeks\" VARCHAR(50)",
                "\"newbornScreening\" VARCHAR(100)", "\"newborn_screening\" VARCHAR(100)",
                "\"newbornHearingScreening\" VARCHAR(100)", "\"newborn_hearing_screening\" VARCHAR(100)",
                "\"newbornCareNotes\" TEXT", "\"newborn_care_notes\" TEXT"
            };

            for (String col : newbornCols) {
                try {
                    jdbcTemplate.execute("ALTER TABLE postpartum_care_records ADD COLUMN IF NOT EXISTS " + col);
                } catch (Exception ignored) {}
            }

            System.out.println("[DatabaseMigration] PatientService, Patient, and PostpartumCare table migration completed successfully.");
        } catch (Exception e) {
            System.err.println("[DatabaseMigration] Migration warning: " + e.getMessage());
        }
    }
}
