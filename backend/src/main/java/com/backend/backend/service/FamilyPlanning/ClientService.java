package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.Client;

public interface ClientService {

    Client addClient(Client client);

    Client getClientById(Integer id);

    List<Client> getClients();

    Client updateClient(Client client);

    void deleteClient(Integer id);

    List<Client> getClientsByNhts(String nhts);

    List<Client> getClientsByIs4PSMember(String is4PSMember);

    List<Client> getClientsByCivilStatus(String civilStatus);
}
