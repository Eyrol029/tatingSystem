package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.FamilyPlanning.MethodCurrentlyUsed;
import com.backend.backend.repository.FamilyPlanning.MethodCurrentlyUsedRepository;

@Service
public class MethodCurrentlyUsedServiceImpl implements MethodCurrentlyUsedService {

    @Autowired
    private MethodCurrentlyUsedRepository methodCurrentlyUsedRepository;

    @Override
    public MethodCurrentlyUsed addMethod(MethodCurrentlyUsed method) {
        return methodCurrentlyUsedRepository.save(method);
    }

    @Override
    public MethodCurrentlyUsed getMethodById(Integer id) {
        return methodCurrentlyUsedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MethodCurrentlyUsed not found: " + id));
    }

    @Override
    public List<MethodCurrentlyUsed> getMethods() {
        return methodCurrentlyUsedRepository.findAll();
    }

    @Override
    public MethodCurrentlyUsed updateMethod(MethodCurrentlyUsed method) {
        return methodCurrentlyUsedRepository.save(method);
    }

    @Override
    public void deleteMethod(Integer id) {
        methodCurrentlyUsedRepository.deleteById(id);
    }

    @Override
    public List<MethodCurrentlyUsed> getMethodsByTypeID(Integer typeID) {
        return methodCurrentlyUsedRepository.findByTypeID(typeID);
    }
}
