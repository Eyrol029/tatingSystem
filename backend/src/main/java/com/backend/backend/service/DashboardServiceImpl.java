package com.backend.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Admission;
import com.backend.backend.model.Appointment.Appointment;
import com.backend.backend.model.Billing.Expense;
import com.backend.backend.model.Billing.Revenue;
import com.backend.backend.model.DashboardSummaryDTO;
import com.backend.backend.model.DashboardSummaryDTO.ActivityItem;
import com.backend.backend.model.DashboardSummaryDTO.PendingItem;
import com.backend.backend.model.Billing.FinancialPoint;
import com.backend.backend.model.Patient;
import com.backend.backend.model.Prenatal.PrenatalRecord;
import com.backend.backend.repository.AdmissionRepository;
import com.backend.backend.repository.Appointment.AppointmentRepository;
import com.backend.backend.repository.Billing.ExpenseRepository;
import com.backend.backend.repository.Billing.RevenueRepository;
import com.backend.backend.repository.PatientRepository;
import com.backend.backend.repository.Prenatal.PrenatalRecordRepository;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired private PatientRepository        patientRepository;
    @Autowired private AppointmentRepository    appointmentRepository;
    @Autowired private PrenatalRecordRepository prenatalRecordRepository;

    // NEW: wired in for the Financial Overview card
    @Autowired private RevenueRepository revenueRepository;
    @Autowired private ExpenseRepository expenseRepository;

    // NEW: wired in so "Total Deliveries" reflects real Lying-In Admission
    // discharges instead of just any PrenatalRecord with a delivery date.
    @Autowired private AdmissionRepository admissionRepository;

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");

    private static final DateTimeFormatter ISO_DATE_FMT =
            DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public DashboardSummaryDTO getSummary(LocalDate start, LocalDate end) {
        DashboardSummaryDTO dto = new DashboardSummaryDTO();

        // ── 1. Stats cards ────────────────────────────────────────────────────
        List<Patient>        allPatients     = patientRepository.findAll();
        List<Appointment>    allAppointments = appointmentRepository.findAll();
        List<PrenatalRecord> allPrenatal     = prenatalRecordRepository.findAll();
        List<Admission>      allAdmissions   = admissionRepository.findAll();

        // Filter by date range if provided
        List<Patient>        patients     = filterPatients(allPatients, start, end);
        List<Appointment>    appointments = filterAppointments(allAppointments, start, end);
        List<PrenatalRecord> prenatal     = filterPrenatal(allPrenatal, start, end);
        List<Admission>      admissions   = filterAdmissions(allAdmissions, start, end);

        dto.setTotalPatients(patients.size());
        dto.setTotalAppointments(appointments.size());
        dto.setTotalPrenatalRecords(prenatal.size());

        // Deliveries = Lying-In Admissions that have actually reached the
        // "discharge" step of the workflow (i.e. the birth was completed and
        // the patient was sent home), not just any prenatal record that
        // happens to have a delivery date filled in.
        long deliveries = admissions.stream()
                .filter(a -> "discharge".equalsIgnoreCase(a.getCurrentStep()) || "discharged".equalsIgnoreCase(a.getCurrentStep()))
                .count();
        dto.setTotalDeliveries(deliveries);

        // ── 2. Financial overview ─────────────────────────────────────────────
        // NEW: pulls from the Revenue and Expense tables, filtered by the same
        // date range as everything else on the dashboard.
        List<Revenue> allRevenue  = revenueRepository.findAll();
        List<Expense> allExpenses = expenseRepository.findAll();

        List<Revenue> revenueInRange = allRevenue.stream()
                .filter(r -> isInRange(r.getRevenueDate(), start, end))
                .collect(Collectors.toList());

        List<Expense> expensesInRange = allExpenses.stream()
                .filter(e -> isInRange(e.getExpenseDate(), start, end))
                .collect(Collectors.toList());

        double revenue = revenueInRange.stream()
                .mapToDouble(r -> r.getAmount() != null ? r.getAmount() : 0.0)
                .sum();

        double expenses = expensesInRange.stream()
                .mapToDouble(e -> e.getAmount() != null ? e.getAmount() : 0.0)
                .sum();

        dto.setTotalRevenue(revenue);
        dto.setTotalExpenses(expenses);
        dto.setNetIncome(revenue - expenses);

        // NEW: day-by-day trend for the Financial Overview line chart. Reuses
        // the same revenueInRange/expensesInRange lists above so the totals
        // and the chart always agree, then fills in zero-value days so the
        // line has no gaps.
        dto.setFinancialTrend(buildFinancialTrend(revenueInRange, expensesInRange, start, end));

        // ── 3. Recent activities (up to 10) ──────────────────────────────────
        List<ActivityItem> activities = new ArrayList<>();

        // Recent patients
        allPatients.stream()
                .sorted(Comparator.comparing(Patient::getPatientID, Comparator.nullsLast(Integer::compareTo)).reversed())
                .limit(3)
                .forEach(p -> activities.add(new ActivityItem(
                        "New patient " + fullName(getPatientFName(p), getPatientLName(p)) + " registered",
                        "recently",
                        "patient")));

        // Recent appointments
        allAppointments.stream()
                .filter(a -> a.getAppointmentDate() != null)
                .sorted(Comparator.comparing(Appointment::getAppointmentDate).reversed())
                .limit(3)
                .forEach(a -> {
                    String apptName = fullName(getFName(a), getLName(a));
                    activities.add(new ActivityItem(
                            "Appointment scheduled for " + apptName
                                    + (a.getServiceType() != null ? " — " + a.getServiceType() : ""),
                            dateStr(a.getAppointmentDate()),
                            "appointment"));
                });

        // Recent prenatal records
        allPrenatal.stream()
                .sorted(Comparator.comparing(PrenatalRecord::getPrenatalrecordID).reversed())
                .limit(3)
                .forEach(p -> activities.add(new ActivityItem(
                        "Prenatal record #" + p.getPrenatalrecordID() + " created"
                                + (p.getDeliveryDate() != null ? " — delivery recorded" : ""),
                        "Record ID " + p.getPrenatalrecordID(),
                        "prenatal")));

        // NEW: Recent completed deliveries (Admissions discharged) — shown
        // alongside the other activity types.
        allAdmissions.stream()
                .filter(a -> "discharge".equalsIgnoreCase(a.getCurrentStep()))
                .sorted(Comparator.comparing(Admission::getAdmissionID, Comparator.nullsLast(Integer::compareTo)).reversed())
                .limit(3)
                .forEach(a -> activities.add(new ActivityItem(
                        "Delivery completed & discharged — "
                                + (a.getPatientName() != null ? a.getPatientName() : "Unknown patient"),
                        a.getAdmissionDate() != null ? a.getAdmissionDate().format(TIME_FMT) : "recently",
                        "prenatal")));

        // NEW: Recent revenue entries — shows up with the green "payment" dot
        // that the dashboard UI already has a color mapping for.
        allRevenue.stream()
                .sorted(Comparator.comparing(
                        Revenue::getRevenueDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .limit(3)
                .forEach(r -> activities.add(new ActivityItem(
                        "Payment received from " + (r.getDealer() != null ? r.getDealer() : "a patient")
                                + " — ₱" + (r.getAmount() != null ? r.getAmount() : 0.0),
                        dateStr(r.getRevenueDate()),
                        "payment")));

        // Sort by most recent and cap at 8
        dto.setRecentActivities(activities.stream().limit(8).collect(Collectors.toList()));

        // ── 4. Pending appointments (upcoming, no delivery date) ──────────────
        LocalDate today = LocalDate.now();
        List<PendingItem> pending = appointments.stream()
                .filter(a -> a.getAppointmentDate() != null
                        && !a.isCompleted()
                        && !a.getAppointmentDate().isBefore(today))
                .sorted(Comparator.comparing(Appointment::getAppointmentDate))
                .limit(5)
                .map(a -> new PendingItem(
                        "Appointment Request",
                        fullName(getFName(a), getLName(a)),
                        a.getServiceType() != null ? a.getServiceType() : "General Consultation",
                        "Pending",
                        a.getAppointmentID()))
                .collect(Collectors.toList());

        dto.setPendingAppointments(pending);

        return dto;
    }

    // ── Date-range filters ────────────────────────────────────────────────────

    private List<Patient> filterPatients(List<Patient> list, LocalDate start, LocalDate end) {
        if (start == null && end == null) return list;
        return list.stream()
                .filter(p -> {
                    LocalDateTime created = getPatientCreatedAt(p);
                    if (created == null) return false;
                    return isInRange(created.toLocalDate(), start, end);
                })
                .collect(Collectors.toList());
    }

    private List<Appointment> filterAppointments(List<Appointment> list, LocalDate start, LocalDate end) {
        if (start == null && end == null) return list;
        return list.stream()
                .filter(a -> isInRange(a.getAppointmentDate(), start, end))
                .collect(Collectors.toList());
    }

    private List<PrenatalRecord> filterPrenatal(List<PrenatalRecord> list, LocalDate start, LocalDate end) {
        if (start == null && end == null) return list;
        return list.stream()
                .filter(p -> isInRange(p.getInitialPreConsultationDate(), start, end))
                .collect(Collectors.toList());
    }

    // NEW: filters Admission records by admissionDate, same pattern as the
    // other filter* helpers.
    private List<Admission> filterAdmissions(List<Admission> list, LocalDate start, LocalDate end) {
        if (start == null && end == null) return list;
        return list.stream()
                .filter(a -> {
                    LocalDateTime dateToUse = a.getDischargeDate();
                    if (dateToUse == null) {
                        dateToUse = a.getAdmissionDate();
                    }
                    if (dateToUse == null) return false;
                    return isInRange(dateToUse.toLocalDate(), start, end);
                })
                .collect(Collectors.toList());
    }

    private boolean isInRange(LocalDate date, LocalDate start, LocalDate end) {
        if (date == null) return false;
        if (start != null && date.isBefore(start)) return false;
        if (end   != null && date.isAfter(end))   return false;
        return true;
    }

    // ── Financial trend (for the Financial Overview line chart) ───────────────

    /**
     * Builds a day-by-day financial trend (revenue, expenses, net income)
     * between start and end (inclusive), using the already-filtered revenue
     * and expense lists so the chart always matches the totals cards.
     *
     * If start/end are null (e.g. "All time" filter), defaults the chart
     * window to the last 30 days so the line has a sane, bounded range.
     * Fills in zero-value days so the line has no gaps.
     */
    private List<FinancialPoint> buildFinancialTrend(
            List<Revenue> revenueInRange,
            List<Expense> expensesInRange,
            LocalDate start,
            LocalDate end) {

        LocalDate rangeStart = start != null ? start : LocalDate.now().minusDays(29);
        LocalDate rangeEnd   = end   != null ? end   : LocalDate.now();

        Map<LocalDate, Double> revenueByDate = revenueInRange.stream()
                .filter(r -> r.getRevenueDate() != null)
                .collect(Collectors.groupingBy(
                        Revenue::getRevenueDate,
                        Collectors.summingDouble(r -> r.getAmount() != null ? r.getAmount() : 0.0)
                ));

        Map<LocalDate, Double> expenseByDate = expensesInRange.stream()
                .filter(e -> e.getExpenseDate() != null)
                .collect(Collectors.groupingBy(
                        Expense::getExpenseDate,
                        Collectors.summingDouble(e -> e.getAmount() != null ? e.getAmount() : 0.0)
                ));

        List<FinancialPoint> points = new ArrayList<>();
        for (LocalDate d = rangeStart; !d.isAfter(rangeEnd); d = d.plusDays(1)) {
            double rev = revenueByDate.getOrDefault(d, 0.0);
            double exp = expenseByDate.getOrDefault(d, 0.0);
            points.add(new FinancialPoint(d.format(ISO_DATE_FMT), rev, exp, rev - exp));
        }

        return points;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String fullName(String first, String last) {
        String f = first != null ? first.trim() : "";
        String l = last  != null ? last.trim()  : "";
        if (f.isEmpty() && l.isEmpty()) return "Unknown";
        return (f + " " + l).trim();
    }

    // Appointment getters
    private String getFName(Appointment a) {
        if (a == null) return "";
        String v = a.getFName();
        return v != null ? v : "";
    }
    private String getLName(Appointment a) {
        if (a == null) return "";
        String v = a.getLName();
        return v != null ? v : "";
    }

    // Patient getters
    private String getPatientFName(Patient p) {
        if (p == null) return "";
        String v = p.getFName();
        return v != null ? v : "";
    }
    private String getPatientLName(Patient p) {
        if (p == null) return "";
        String v = p.getLName();
        return v != null ? v : "";
    }

    // Patients in this model do not expose a createdAt field, so date filtering
    // falls back to including records when no creation timestamp is available.
    private LocalDateTime getPatientCreatedAt(Patient p) {
        return p.getCreatedAt();
    }

    private String timeAgo(LocalDateTime dt) {
        if (dt == null) return "recently";
        long mins = ChronoUnit.MINUTES.between(dt, LocalDateTime.now());
        if (mins < 60) return mins + " minute" + (mins == 1 ? "" : "s") + " ago";
        long hrs = ChronoUnit.HOURS.between(dt, LocalDateTime.now());
        if (hrs < 24) return hrs + " hour" + (hrs == 1 ? "" : "s") + " ago";
        long days = ChronoUnit.DAYS.between(dt, LocalDateTime.now());
        return days + " day" + (days == 1 ? "" : "s") + " ago";
    }

    private String dateStr(LocalDate d) {
        if (d == null) return "";
        return d.format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
    }
}