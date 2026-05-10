package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.FamilyPlanning.Client;
import com.backend.backend.repository.FamilyPlanning.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client getClientById(Integer id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found: " + id));
    }

    @Override
    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client updateClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public void deleteClient(Integer id) {
        clientRepository.deleteById(id);
    }

    @Override
    public List<Client> getClientsByNhts(String nhts) {
        return clientRepository.findByNhts(nhts);
    }

    @Override
    public List<Client> getClientsByIs4PSMember(String is4PSMember) {
        return clientRepository.findByIs4PSMember(is4PSMember);
    }

    @Override
    public List<Client> getClientsByCivilStatus(String civilStatus) {
        return clientRepository.findByCivilStatus(civilStatus);
    }
}
