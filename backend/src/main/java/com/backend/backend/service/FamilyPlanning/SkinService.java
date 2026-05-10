package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.Skin;

public interface SkinService {

    Skin addSkin(Skin skin);

    Skin getSkinById(Integer id);

    List<Skin> getAllSkin();

    Skin updateSkin(Skin skin);

    void deleteSkin(Integer id);

    List<Skin> getByPExamID(Integer pExamID);
}
