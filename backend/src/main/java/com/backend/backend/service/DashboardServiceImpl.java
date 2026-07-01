package com.backend.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Appointment.Appointment;
import com.backend.backend.model.DashboardSummaryDTO;
import com.backend.backend.model.DashboardSummaryDTO.ActivityItem;
import com.backend.backend.model.DashboardSummaryDTO.PendingItem;
import com.backend.backend.model.Patient;
import com.backend.backend.model.Prenatal.PrenatalRecord;
import com.backend.backend.repository.Appointment.AppointmentRepository;
import com.backend.backend.repository.PatientRepository;
import com.backend.backend.repository.Prenatal.PrenatalRecordRepository;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired private PatientRepository        patientRepository;
    @Autowired private AppointmentRepository    appointmentRepository;
    @Autowired private PrenatalRecordRepository prenatalRecordRepository;

    // Inject payment repository if you have one — see comment below
    // @Autowired private PaymentRepository paymentRepository;

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");

    @Override
    public DashboardSummaryDTO getSummary(LocalDate start, LocalDate end) {
        DashboardSummaryDTO dto = new DashboardSummaryDTO();

        // ── 1. Stats cards ────────────────────────────────────────────────────
        List<Patient>        allPatients     = patientRepository.findAll();
        List<Appointment>    allAppointments = appointmentRepository.findAll();
        List<PrenatalRecord> allPrenatal     = prenatalRecordRepository.findAll();

        // Filter by date range if provided
        List<Patient>        patients     = filterPatients(allPatients, start, end);
        List<Appointment>    appointments = filterAppointments(allAppointments, start, end);
        List<PrenatalRecord> prenatal     = filterPrenatal(allPrenatal, start, end);

        dto.setTotalPatients(patients.size());
        dto.setTotalAppointments(appointments.size());
        dto.setTotalPrenatalRecords(prenatal.size());

        // Deliveries = prenatal records that have an actual delivery date recorded
        long deliveries = prenatal.stream()
                .filter(p -> p.getDeliveryDate() != null)
                .count();
        dto.setTotalDeliveries(deliveries);

        // ── 2. Financial overview ─────────────────────────────────────────────
        // If you have a Payment/Billing table, inject its repository and replace
        // these placeholders. For now we compute 0 and note where to hook in.
        double revenue  = 0.0;
        double expenses = 0.0;

        // Example (uncomment when you have a payment repository):
        // List<Payment> payments = paymentRepository.findAll();
        // revenue = payments.stream()
        //     .filter(p -> isInRange(toLocalDate(p.getPaymentDate()), start, end))
        //     .mapToDouble(p -> p.getAmount() != null ? p.getAmount().doubleValue() : 0)
        //     .sum();

        dto.setTotalRevenue(revenue);
        dto.setTotalExpenses(expenses);
        dto.setNetIncome(revenue - expenses);

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

        // Sort by most recent and cap at 8
        dto.setRecentActivities(activities.stream().limit(8).collect(Collectors.toList()));

        // ── 4. Pending appointments (upcoming, no delivery date) ──────────────
        LocalDate today = LocalDate.now();
        List<PendingItem> pending = appointments.stream()
                .filter(a -> a.getAppointmentDate() != null
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
                    if (created == null) return true; // include if no date
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

    private boolean isInRange(LocalDate date, LocalDate start, LocalDate end) {
        if (date == null) return false;
        if (start != null && date.isBefore(start)) return false;
        if (end   != null && date.isAfter(end))   return false;
        return true;
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
        return null;
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