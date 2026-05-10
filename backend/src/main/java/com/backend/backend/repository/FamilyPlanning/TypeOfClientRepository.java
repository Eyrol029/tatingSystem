package com.backend.backend.repository.FamilyPlanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.FamilyPlanning.TypeOfClient;

public interface TypeOfClientRepository extends CrudRepository<TypeOfClient, Integer> {

    List<TypeOfClient> findAll();

    List<TypeOfClient> findByClientID(Integer clientID);

    List<TypeOfClient> findByIsNewAcceptor(Boolean isNewAcceptor);

    List<TypeOfClient> findByIsCurrentUser(Boolean isCurrentUser);
}
