package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.Abdomen;

public interface AbdomenService {

    Abdomen addAbdomen(Abdomen abdomen);

    Abdomen getAbdomenById(Integer id);

    List<Abdomen> getAbdomens();

    Abdomen updateAbdomen(Abdomen abdomen);

    void deleteAbdomen(Integer id);

    List<Abdomen> getByPExamID(Integer pExamID);
}
