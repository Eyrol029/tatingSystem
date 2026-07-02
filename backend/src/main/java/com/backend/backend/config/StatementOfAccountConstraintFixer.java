package com.backend.backend.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class StatementOfAccountConstraintFixer {

    private static final Logger logger = LoggerFactory.getLogger(StatementOfAccountConstraintFixer.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StatementOfAccountConstraintFixer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void fixPaymentStatusConstraint() {
        try {
            jdbcTemplate.execute("ALTER TABLE statement_of_account DROP CONSTRAINT IF EXISTS statement_of_account_payment_status_check");
            jdbcTemplate.execute("ALTER TABLE statement_of_account ADD CONSTRAINT statement_of_account_payment_status_check CHECK (payment_status IN ('Pending', 'Partial', 'Paid'))");
            logger.info("Updated StatementOfAccount payment_status constraint to allow Partial status.");
        } catch (Exception ex) {
            logger.warn("Could not update StatementOfAccount payment_status constraint. This may already be correct or the database connection is unavailable.", ex);
        }
    }
}
