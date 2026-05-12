package com.backend.backend.repository.Prenatal;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.Prenatal.NormalVitalSign;

public interface NormalVitalSignRepository extends CrudRepository<NormalVitalSign, Integer> {

    List<NormalVitalSign> findAll();

    List<NormalVitalSign> findByClinicalHistoryID(Integer clinicalHistoryID);
}
