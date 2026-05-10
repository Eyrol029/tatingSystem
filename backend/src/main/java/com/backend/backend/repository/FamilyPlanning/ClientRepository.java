package com.backend.backend.repository.FamilyPlanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.FamilyPlanning.Client;

public interface ClientRepository extends CrudRepository<Client, Integer> {

    List<Client> findAll();

    List<Client> findByNhts(String nhts);

    List<Client> findByIs4PSMember(String is4PSMember);

    List<Client> findByCivilStatus(String civilStatus);
}
