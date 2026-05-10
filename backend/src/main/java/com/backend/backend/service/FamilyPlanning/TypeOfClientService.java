package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.TypeOfClient;

public interface TypeOfClientService {

    TypeOfClient addTypeOfClient(TypeOfClient typeOfClient);

    TypeOfClient getTypeOfClientById(Integer id);

    List<TypeOfClient> getTypeOfClients();

    TypeOfClient updateTypeOfClient(TypeOfClient typeOfClient);

    void deleteTypeOfClient(Integer id);

    List<TypeOfClient> getTypeOfClientsByClientID(Integer clientID);

    List<TypeOfClient> getTypeOfClientsByIsNewAcceptor(Boolean isNewAcceptor);

    List<TypeOfClient> getTypeOfClientsByIsCurrentUser(Boolean isCurrentUser);
}
