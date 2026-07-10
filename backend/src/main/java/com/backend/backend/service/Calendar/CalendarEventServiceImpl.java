package com.backend.backend.service.Calendar;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Appointment.Appointment;
import com.backend.backend.model.CalendarEvent;
import com.backend.backend.model.CalendarEventDto;
import com.backend.backend.model.Patient;
import com.backend.backend.model.PatientService;
import com.backend.backend.model.Prenatal.PrenatalRecord;
import com.backend.backend.model.Prenatal.VitalSigns;
import com.backend.backend.repository.Appointment.AppointmentRepository;
import com.backend.backend.repository.Calendar.CalendarEventRepository;
import com.backend.backend.repository.PatientRepository;
import com.backend.backend.repository.PatientServiceRepository;
import com.backend.backend.repository.Prenatal.ConsultationRecordRepository;
import com.backend.backend.repository.Prenatal.PrenatalRecordRepository;
import com.backend.backend.repository.Prenatal.VitalSignsRepository;

@Service
public class CalendarEventServiceImpl implements CalendarEventService {

    @Autowired
    private CalendarEventRepository manualEventRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PrenatalRecordRepository prenatalRecordRepository;

    @Autowired
    private ConsultationRecordRepository consultationRecordRepository;

    @Autowired
    private VitalSignsRepository vitalSignsRepository;

    @Autowired
    private PatientServiceRepository patientServiceRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public List<CalendarEventDto> getAllCalendarEvents() {
        return getCalendarEventsInRange(null, null);
    }

    @Override
    public List<CalendarEventDto> getCalendarEventsInRange(LocalDate start, LocalDate end) {
        List<CalendarEventDto> events = new ArrayList<>();
        events.addAll(loadAppointmentEvents(start, end));
        events.addAll(loadPrenatalEvents(start, end));
        events.addAll(loadManualEvents(start, end));
        return events.stream()
                .sorted((a, b) -> a.getEventDate().compareTo(b.getEventDate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CalendarEvent> getAllManualEvents() {
        return manualEventRepository.findAll();
    }

    @Override
    public CalendarEvent addManualEvent(CalendarEvent event) {
        return manualEventRepository.save(event);
    }

    @Override
    public CalendarEvent updateManualEvent(CalendarEvent event) {
        return manualEventRepository.save(event);
    }

    @Override
    public void deleteManualEvent(Integer id) {
        manualEventRepository.deleteById(id);
    }

    private List<CalendarEventDto> loadAppointmentEvents(LocalDate start, LocalDate end) {
        return appointmentRepository.findAll().stream()
                .filter(a -> a.getAppointmentDate() != null)
                .map(this::mapAppointmentToDto)
                .filter(event -> isInRange(event.getEventDate(), start, end))
                .collect(Collectors.toList());
    }

    private List<CalendarEventDto> loadPrenatalEvents(LocalDate start, LocalDate end) {
        return prenatalRecordRepository.findAll().stream()
                .map(this::mapPrenatalToDtos)
                .flatMap(List::stream)
                .filter(event -> event.getEventDate() != null)
                .filter(event -> isInRange(event.getEventDate(), start, end))
                .collect(Collectors.toList());
    }

    private List<CalendarEventDto> mapPrenatalToDtos(PrenatalRecord record) {
        PatientService patientService = patientServiceRepository.findById(record.getServiceID()).orElse(null);
        if (patientService == null) {
            return List.of();
        }

        Patient patient = patientRepository.findById(patientService.getPatientID()).orElse(null);
        if (patient == null) {
            return List.of();
        }

        List<CalendarEventDto> events = new ArrayList<>();
        int index = 1;

        addPrenatalEvent(events, record, record.getExpectedDeliveryDate(), "Expected Delivery Date", "expected-delivery", "Expected delivery date", index++, patient);
        addPrenatalEvent(events, record, record.getEdc(), "Estimated Delivery Date", "edc", "Estimated delivery date", index++, patient);
        addPrenatalEvent(events, record, record.getDeliveryDate(), "Delivery Date", "delivery", "Actual delivery date", index++, patient);
        addPrenatalEvent(events, record, record.getPrenatalExamDate(), "Prenatal Exam", "prenatal-exam", "Prenatal exam date", index++, patient);
        addInitialVisitEvent(events, record, index++, patient);
        addNextVisitEvent(events, record, index++, patient);

        return events;
    }

    private void addPrenatalEvent(List<CalendarEventDto> events, PrenatalRecord record, LocalDate date, String title, String type, String description, int index, Patient patient) {
        if (date == null) {
            return;
        }

        LocalDate eventDate = convertToLocalDate(date);
        if (eventDate == null) {
            return;
        }

        String patientName = resolvePrenatalPatientNameForPatient(patient);

        CalendarEventDto dto = new CalendarEventDto();
        dto.setEventID(record.getPrenatalrecordID() * 100 + index);
        dto.setTitle(title);
        dto.setEventDate(eventDate);
        dto.setEventType(type);
        dto.setPatientId(patient.getPatientID());
        dto.setPatientName(patientName);
        dto.setDescription(description);
        dto.setSource("prenatal");
        events.add(dto);
    }

    private void addInitialVisitEvent(List<CalendarEventDto> events, PrenatalRecord record, int index, Patient patient) {
        if (record.getInitialPreConsultationDate() == null) {
            return;
        }

        addPrenatalEvent(events, record, record.getInitialPreConsultationDate(), "Initial Prenatal Visit", "prenatal-visit", "Initial prenatal visit date", index, patient);
    }

    private void addNextVisitEvent(List<CalendarEventDto> events, PrenatalRecord record, int index, Patient patient) {
        java.util.Date nextVisitDate = findNextVitalSignsVisitDate(record);
        if (nextVisitDate == null) {
            return;
        }

        LocalDate eventDate = convertToLocalDate(nextVisitDate);
        if (eventDate == null) {
            return;
        }

        String patientName = resolvePrenatalPatientNameForPatient(patient);

        CalendarEventDto dto = new CalendarEventDto();
        dto.setEventID(record.getPrenatalrecordID() * 100 + index);
        dto.setTitle("Next Prenatal Visit");
        dto.setEventDate(eventDate);
        dto.setEventType("next-visit");
        dto.setPatientId(patient.getPatientID());
        dto.setPatientName(patientName);
        dto.setDescription("Next prenatal visit date");
        dto.setSource("prenatal");
        events.add(dto);
    }

    private java.util.Date findNextVitalSignsVisitDate(PrenatalRecord record) {
        return consultationRecordRepository.findByPrenatalRecordID(record.getPrenatalrecordID()).stream()
                .flatMap(consultation -> vitalSignsRepository.findByConsultationRecordID(consultation.getConsultationRecordID()).stream())
                .map(VitalSigns::getNextVisitDate)
                .filter(Objects::nonNull)
                .sorted((d1, d2) -> convertToLocalDate(d1).compareTo(convertToLocalDate(d2)))
                .findFirst()
                .orElse(null);
    }

    private List<CalendarEventDto> loadManualEvents(LocalDate start, LocalDate end) {
        return manualEventRepository.findAll().stream()
                .map(this::mapManualEventToDto)
                .filter(event -> event.getEventDate() != null)
                .filter(event -> isInRange(event.getEventDate(), start, end))
                .collect(Collectors.toList());
    }

    private CalendarEventDto mapAppointmentToDto(Appointment appointment) {
        CalendarEventDto dto = new CalendarEventDto();
        dto.setEventID(appointment.getAppointmentID());
        dto.setTitle(appointment.getServiceType() != null && !appointment.getServiceType().isBlank()
                ? appointment.getServiceType()
                : "Appointment");
        dto.setEventDate(appointment.getAppointmentDate());
        dto.setEventType("appointment");
        dto.setPatientId(appointment.getPatientID());
        dto.setPatientName(buildPatientName(appointment.getFName(), appointment.getLName()));
        dto.setDescription(appointment.getAppointmentTime() != null
                ? "Time: " + appointment.getAppointmentTime().toString()
                : "Scheduled appointment");
        dto.setSource("appointment");
        return dto;
    }

    private CalendarEventDto mapPrenatalToDto(PrenatalRecord record) {
        LocalDate dueDate = record.getExpectedDeliveryDate() != null ? record.getExpectedDeliveryDate()
                : record.getEdc() != null ? record.getEdc()
                : record.getPrenatalExamDate() != null ? record.getPrenatalExamDate()
                : record.getInitialPreConsultationDate();

        LocalDate eventDate = convertToLocalDate(dueDate);

        CalendarEventDto dto = new CalendarEventDto();
        dto.setEventID(record.getPrenatalrecordID());
        dto.setTitle(determinePrenatalTitle(record));
        dto.setEventDate(eventDate);
        dto.setEventType("labor");
        dto.setPatientName(resolvePrenatalPatientName(record));
        dto.setDescription("Prenatal data from record " + record.getPrenatalrecordID());
        dto.setSource("prenatal");
        return dto;
    }

    private String determinePrenatalTitle(PrenatalRecord record) {
        if (record.getExpectedDeliveryDate() != null || record.getEdc() != null) {
            return "Expected Day of Labor";
        }
        if (record.getPrenatalExamDate() != null) {
            return "Prenatal Exam";
        }
        if (record.getInitialPreConsultationDate() != null) {
            return "Initial Prenatal Visit";
        }
        return "Prenatal Event";
    }

    private CalendarEventDto mapManualEventToDto(CalendarEvent event) {
        CalendarEventDto dto = new CalendarEventDto();
        dto.setEventID(event.getEventID());
        dto.setTitle(event.getTitle());
        dto.setEventDate(event.getEventDate());
        dto.setEventType(event.getEventType() != null ? event.getEventType() : "manual");
        dto.setPatientName(event.getPatientName());
        dto.setDescription(event.getDescription());
        dto.setSource("manual");
        return dto;
    }

    private boolean isInRange(LocalDate date, LocalDate start, LocalDate end) {
        if (date == null) {
            return false;
        }
        if (start != null && date.isBefore(start)) {
            return false;
        }
        if (end != null && date.isAfter(end)) {
            return false;
        }
        return true;
    }

    private LocalDate convertToLocalDate(LocalDate date) {
        return date;
    }

    private LocalDate convertToLocalDate(java.util.Date date) {
        if (date == null) {
            return null;
        }

        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }

        return java.time.Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private String resolvePrenatalPatientName(PrenatalRecord record) {
        if (record == null || record.getServiceID() == null) {
            return "Unknown patient";
        }

        return patientServiceRepository.findById(record.getServiceID())
                .map(patientService -> {
                    Integer patientId = patientService.getPatientID();
                    if (patientId != null) {
                        return patientRepository.findById(patientId)
                                .map(patient -> buildPatientName(patient.getFName(), patient.getLName()))
                                .orElse(formatPatientServiceFallback(patientService));
                    }
                    return formatPatientServiceFallback(patientService);
                })
                .orElse("Unknown patient");
    }

    private String resolvePrenatalPatientNameForPatient(Patient patient) {
        if (patient == null) {
            return "Unknown patient";
        }
        return buildPatientName(patient.getFName(), patient.getLName());
    }

    private String formatPatientServiceFallback(PatientService patientService) {
        String serviceName = patientService.getServiceName();
        if (serviceName != null && !serviceName.isBlank()) {
            return serviceName + " (Service #" + patientService.getPatientServiceID() + ")";
        }
        return "Unknown patient (Service #" + patientService.getPatientServiceID() + ")";
    }

    private String buildPatientName(String firstName, String lastName) {
        StringBuilder name = new StringBuilder();
        if (firstName != null && !firstName.isBlank()) {
            name.append(firstName.trim());
        }
        if (lastName != null && !lastName.isBlank()) {
            if (name.length() > 0) {
                name.append(" ");
            }
            name.append(lastName.trim());
        }
        return name.length() > 0 ? name.toString() : "Patient";
    }
}
