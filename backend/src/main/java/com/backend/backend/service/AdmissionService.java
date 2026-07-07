package com.backend.backend.service;

import java.util.List;

import com.backend.backend.model.Admission;

public interface AdmissionService {

    Admission addAdmission(Admission admission);

    Admission getAdmissionById(Integer id);

    List<Admission> getAllAdmissions();

    Admission updateAdmission(Admission admission);

    void deleteAdmission(Integer id);
}