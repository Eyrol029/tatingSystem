package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.FamilyPlanning.TypeOfClient;
import com.backend.backend.repository.FamilyPlanning.TypeOfClientRepository;

@Service
public class TypeOfClientServiceImpl implements TypeOfClientService {

    @Autowired
    private TypeOfClientRepository typeOfClientRepository;

    @Override
    public TypeOfClient addTypeOfClient(TypeOfClient typeOfClient) {
        return typeOfClientRepository.save(typeOfClient);
    }

    @Override
    public TypeOfClient getTypeOfClientById(Integer id) {
        return typeOfClientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TypeOfClient not found: " + id));
    }

    @Override
    public List<TypeOfClient> getTypeOfClients() {
        return typeOfClientRepository.findAll();
    }

    @Override
    public TypeOfClient updateTypeOfClient(TypeOfClient typeOfClient) {
        return typeOfClientRepository.save(typeOfClient);
    }

    @Override
    public void deleteTypeOfClient(Integer id) {
        typeOfClientRepository.deleteById(id);
    }

    @Override
    public List<TypeOfClient> getTypeOfClientsByClientID(Integer clientID) {
        return typeOfClientRepository.findByClientID(clientID);
    }

    @Override
    public List<TypeOfClient> getTypeOfClientsByIsNewAcceptor(Boolean isNewAcceptor) {
        return typeOfClientRepository.findByIsNewAcceptor(isNewAcceptor);
    }

    @Override
    public List<TypeOfClient> getTypeOfClientsByIsCurrentUser(Boolean isCurrentUser) {
        return typeOfClientRepository.findByIsCurrentUser(isCurrentUser);
    }
}
