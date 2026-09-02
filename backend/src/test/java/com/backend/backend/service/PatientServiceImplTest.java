package com.backend.backend.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.backend.model.Patient;
import com.backend.backend.model.PatientService;
import com.backend.backend.model.Billing.StatementOfAccount;
import com.backend.backend.repository.AdmissionRepository;
import com.backend.backend.repository.Appointment.AppointmentRepository;
import com.backend.backend.repository.Billing.BillingRepository;
import com.backend.backend.repository.Billing.PaymentInstallmentRepository;
import com.backend.backend.repository.Billing.RevenueRepository;
import com.backend.backend.repository.Billing.StatementOfAccountRepository;
import com.backend.backend.repository.Calendar.CalendarEventRepository;
import com.backend.backend.repository.PatientRepository;
import com.backend.backend.repository.PatientServiceRepository;
import com.backend.backend.repository.Prenatal.PrenatalRecordRepository;
import com.backend.backend.repository.ReferralRepository;
import com.backend.backend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientServiceRepository patientServiceRepository;

    @Mock
    private PrenatalRecordRepository prenatalRecordRepository;

    @Mock
    private AdmissionRepository admissionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReferralRepository referralRepository;

    @Mock
    private CalendarEventRepository calendarEventRepository;

    @Mock
    private StatementOfAccountRepository statementOfAccountRepository;

    @Mock
    private BillingRepository billingRepository;

    @Mock
    private PaymentInstallmentRepository paymentInstallmentRepository;

    @Mock
    private RevenueRepository revenueRepository;

    @InjectMocks
    private PatientServiceImpl patientServiceImpl;

    @Test
    void deletePatient_shouldCleanAllDependentRecordsBeforeDeletingPatient() {
        Patient patient = new Patient();
        patient.setPatientID(7);
        patient.setNumberOfPregnancy(0);

        PatientService service = new PatientService();
        service.setPatientServiceID(101);
        service.setPatientID(7);

        StatementOfAccount soa = new StatementOfAccount();
        soa.setSoaID(55);
        soa.setPatientID(7);
        soa.setPatientServiceID(101);

        when(patientRepository.findById(7)).thenReturn(Optional.of(patient));
        when(patientServiceRepository.findByPatientID(7)).thenReturn(List.of(service));

        patientServiceImpl.deletePatient(7);

        verify(admissionRepository).deleteByPatientID(7);
        verify(appointmentRepository).deleteByPatientID(7);
        verify(calendarEventRepository).deleteByPatientID(7);
        verify(userRepository).deleteByPatientID(7);
        verify(referralRepository).deleteByPatientId(7L);
        verify(statementOfAccountRepository).deleteByPatientID(7);
        verify(paymentInstallmentRepository).deleteByPatientId(7);
        verify(billingRepository).deleteByPatientId(7);
        verify(revenueRepository).deleteByPatientID(7);
        verify(prenatalRecordRepository).deleteByServiceID(101);
        verify(patientServiceRepository).deleteByPatientID(7);
        verify(patientRepository).deleteById(7);
    }
}
