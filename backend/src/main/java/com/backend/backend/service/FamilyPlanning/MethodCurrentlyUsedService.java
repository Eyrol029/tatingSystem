package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.MethodCurrentlyUsed;

public interface MethodCurrentlyUsedService {

    MethodCurrentlyUsed addMethod(MethodCurrentlyUsed method);

    MethodCurrentlyUsed getMethodById(Integer id);

    List<MethodCurrentlyUsed> getMethods();

    MethodCurrentlyUsed updateMethod(MethodCurrentlyUsed method);

    void deleteMethod(Integer id);

    List<MethodCurrentlyUsed> getMethodsByTypeID(Integer typeID);
}
