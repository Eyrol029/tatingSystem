package com.backend.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.backend.backend.model.CalendarEvent;
import com.backend.backend.model.CalendarEventDto;
import com.backend.backend.model.Patient;
import com.backend.backend.model.PatientService;
import com.backend.backend.model.Billing.StatementOfAccount;
import com.backend.backend.repository.Appointment.AppointmentRepository;
import com.backend.backend.repository.Billing.StatementOfAccountRepository;
import com.backend.backend.repository.PatientRepository;
import com.backend.backend.repository.PatientServiceRepository;
import com.backend.backend.repository.Prenatal.ConsultationRecordRepository;
import com.backend.backend.repository.Prenatal.PrenatalRecordRepository;
import com.backend.backend.repository.Prenatal.VitalSignsRepository;
import com.backend.backend.repository.Calendar.CalendarEventRepository;
import com.backend.backend.service.Calendar.CalendarEventServiceImpl;

class CalendarEventServiceImplTest {

    @Test
    void shouldIncludeBillingDueDatesInCalendar() {
        CalendarEventServiceImpl service = new CalendarEventServiceImpl();

        CalendarEventRepository manualEventRepository = mock(CalendarEventRepository.class);
        AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);
        PrenatalRecordRepository prenatalRecordRepository = mock(PrenatalRecordRepository.class);
        ConsultationRecordRepository consultationRecordRepository = mock(ConsultationRecordRepository.class);
        VitalSignsRepository vitalSignsRepository = mock(VitalSignsRepository.class);
        PatientServiceRepository patientServiceRepository = mock(PatientServiceRepository.class);
        PatientRepository patientRepository = mock(PatientRepository.class);
        StatementOfAccountRepository soaRepository = mock(StatementOfAccountRepository.class);

        when(manualEventRepository.findAll()).thenReturn(List.of());
        when(appointmentRepository.findAll()).thenReturn(List.of());
        when(prenatalRecordRepository.findAll()).thenReturn(List.of());
        when(patientServiceRepository.findAll()).thenReturn(List.of());
        when(consultationRecordRepository.findByPrenatalRecordID(0)).thenReturn(List.of());
        when(vitalSignsRepository.findByConsultationRecordID(0)).thenReturn(List.of());

        Patient patient = new Patient();
        patient.setPatientID(77);
        patient.setFName("Maria");
        patient.setLName("Dela Cruz");
        when(patientRepository.findById(77)).thenReturn(Optional.of(patient));

        StatementOfAccount soa = new StatementOfAccount();
        soa.setSoaID(12);
        soa.setPatientID(77);
        soa.setBalanceAmount(13500.0);
        soa.setDueDate(LocalDateTime.of(2026, 8, 30, 9, 0));
        when(soaRepository.findAll()).thenReturn(List.of(soa));

        ReflectionTestUtils.setField(service, "manualEventRepository", manualEventRepository);
        ReflectionTestUtils.setField(service, "appointmentRepository", appointmentRepository);
        ReflectionTestUtils.setField(service, "prenatalRecordRepository", prenatalRecordRepository);
        ReflectionTestUtils.setField(service, "consultationRecordRepository", consultationRecordRepository);
        ReflectionTestUtils.setField(service, "vitalSignsRepository", vitalSignsRepository);
        ReflectionTestUtils.setField(service, "patientServiceRepository", patientServiceRepository);
        ReflectionTestUtils.setField(service, "patientRepository", patientRepository);
        ReflectionTestUtils.setField(service, "statementOfAccountRepository", soaRepository);

        List<CalendarEventDto> events = service.getCalendarEventsInRange(LocalDate.of(2026, 8, 29), LocalDate.of(2026, 9, 2));

        assertFalse(events.isEmpty());
        assertEquals("Billing Due Date", events.get(0).getTitle());
        assertEquals(LocalDate.of(2026, 8, 30), events.get(0).getEventDate());
    }

    @Test
    void shouldExposeManualEventPatientIdForSoaNavigation() {
        CalendarEventServiceImpl service = new CalendarEventServiceImpl();

        CalendarEventRepository manualEventRepository = mock(CalendarEventRepository.class);
        AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);
        PrenatalRecordRepository prenatalRecordRepository = mock(PrenatalRecordRepository.class);
        ConsultationRecordRepository consultationRecordRepository = mock(ConsultationRecordRepository.class);
        VitalSignsRepository vitalSignsRepository = mock(VitalSignsRepository.class);
        PatientServiceRepository patientServiceRepository = mock(PatientServiceRepository.class);
        PatientRepository patientRepository = mock(PatientRepository.class);
        StatementOfAccountRepository soaRepository = mock(StatementOfAccountRepository.class);

        CalendarEvent manualEvent = new CalendarEvent();
        manualEvent.setEventID(99);
        manualEvent.setTitle("Follow-up Visit");
        manualEvent.setEventDate(LocalDate.of(2026, 8, 24));
        manualEvent.setEventType("manual");
        manualEvent.setPatientID(42);
        manualEvent.setPatientName("Eryol");
        manualEvent.setDescription("Check record");

        when(manualEventRepository.findAll()).thenReturn(List.of(manualEvent));
        when(appointmentRepository.findAll()).thenReturn(List.of());
        when(prenatalRecordRepository.findAll()).thenReturn(List.of());
        when(patientServiceRepository.findAll()).thenReturn(List.of());
        when(consultationRecordRepository.findByPrenatalRecordID(0)).thenReturn(List.of());
        when(vitalSignsRepository.findByConsultationRecordID(0)).thenReturn(List.of());
        when(soaRepository.findAll()).thenReturn(List.of());

        ReflectionTestUtils.setField(service, "manualEventRepository", manualEventRepository);
        ReflectionTestUtils.setField(service, "appointmentRepository", appointmentRepository);
        ReflectionTestUtils.setField(service, "prenatalRecordRepository", prenatalRecordRepository);
        ReflectionTestUtils.setField(service, "consultationRecordRepository", consultationRecordRepository);
        ReflectionTestUtils.setField(service, "vitalSignsRepository", vitalSignsRepository);
        ReflectionTestUtils.setField(service, "patientServiceRepository", patientServiceRepository);
        ReflectionTestUtils.setField(service, "patientRepository", patientRepository);
        ReflectionTestUtils.setField(service, "statementOfAccountRepository", soaRepository);

        List<CalendarEventDto> events = service.getCalendarEventsInRange(LocalDate.of(2026, 8, 24), LocalDate.of(2026, 8, 24));

        assertFalse(events.isEmpty());
        assertEquals(42, events.get(0).getPatientId());
        assertEquals("Follow-up Visit", events.get(0).getTitle());
    }

    @Test
    void shouldResolveBillingDuePatientFromSoaServiceIdWhenPatientIdIsMissing() {
        CalendarEventServiceImpl service = new CalendarEventServiceImpl();

        CalendarEventRepository manualEventRepository = mock(CalendarEventRepository.class);
        AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);
        PrenatalRecordRepository prenatalRecordRepository = mock(PrenatalRecordRepository.class);
        ConsultationRecordRepository consultationRecordRepository = mock(ConsultationRecordRepository.class);
        VitalSignsRepository vitalSignsRepository = mock(VitalSignsRepository.class);
        PatientServiceRepository patientServiceRepository = mock(PatientServiceRepository.class);
        PatientRepository patientRepository = mock(PatientRepository.class);
        StatementOfAccountRepository soaRepository = mock(StatementOfAccountRepository.class);

        Patient patient = new Patient();
        patient.setPatientID(88);
        patient.setFName("Maria");
        patient.setLName("Santos");

        PatientService serviceRecord = new PatientService();
        serviceRecord.setPatientServiceID(15);
        serviceRecord.setPatientID(88);

        StatementOfAccount soa = new StatementOfAccount();
        soa.setSoaID(7);
        soa.setPatientServiceID(15);
        soa.setPatientID(null);
        soa.setBalanceAmount(2500.0);
        soa.setDueDate(LocalDateTime.of(2026, 9, 10, 9, 0));

        when(manualEventRepository.findAll()).thenReturn(List.of());
        when(appointmentRepository.findAll()).thenReturn(List.of());
        when(prenatalRecordRepository.findAll()).thenReturn(List.of());
        when(patientServiceRepository.findAll()).thenReturn(List.of());
        when(patientServiceRepository.findById(15)).thenReturn(Optional.of(serviceRecord));
        when(patientRepository.findById(88)).thenReturn(Optional.of(patient));
        when(consultationRecordRepository.findByPrenatalRecordID(0)).thenReturn(List.of());
        when(vitalSignsRepository.findByConsultationRecordID(0)).thenReturn(List.of());
        when(soaRepository.findAll()).thenReturn(List.of(soa));

        ReflectionTestUtils.setField(service, "manualEventRepository", manualEventRepository);
        ReflectionTestUtils.setField(service, "appointmentRepository", appointmentRepository);
        ReflectionTestUtils.setField(service, "prenatalRecordRepository", prenatalRecordRepository);
        ReflectionTestUtils.setField(service, "consultationRecordRepository", consultationRecordRepository);
        ReflectionTestUtils.setField(service, "vitalSignsRepository", vitalSignsRepository);
        ReflectionTestUtils.setField(service, "patientServiceRepository", patientServiceRepository);
        ReflectionTestUtils.setField(service, "patientRepository", patientRepository);
        ReflectionTestUtils.setField(service, "statementOfAccountRepository", soaRepository);

        List<CalendarEventDto> events = service.getCalendarEventsInRange(LocalDate.of(2026, 9, 9), LocalDate.of(2026, 9, 12));

        assertFalse(events.isEmpty());
        assertEquals(88, events.get(0).getPatientId());
        assertEquals("Maria Santos", events.get(0).getPatientName());
    }
}
