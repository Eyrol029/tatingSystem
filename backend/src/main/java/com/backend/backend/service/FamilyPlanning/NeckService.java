package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.Neck;

public interface NeckService {

    Neck addNeck(Neck neck);

    Neck getNeckById(Integer id);

    List<Neck> getNecks();

    Neck updateNeck(Neck neck);

    void deleteNeck(Integer id);

    List<Neck> getByPExamID(Integer pExamID);
}
