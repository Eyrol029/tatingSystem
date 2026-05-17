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

            System.out.println("[DatabaseMigration] PatientService table migration completed successfully.");
        } catch (Exception e) {
            System.err.println("[DatabaseMigration] Migration warning: " + e.getMessage());
        }
    }
}
