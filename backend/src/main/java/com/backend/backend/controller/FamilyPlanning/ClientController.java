package com.backend.backend.controller.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend.model.FamilyPlanning.Client;
import com.backend.backend.service.FamilyPlanning.ClientService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;
    @CrossOrigin(origins = "*")
    @PostMapping
    public Client addClient(@RequestBody Client client) {
        return clientService.addClient(client);
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public Client getClient(@PathVariable Integer id) {
        return clientService.getClientById(id);
    }
    @CrossOrigin(origins = "*")
    @GetMapping
    public List<Client> getClients() {
        return clientService.getClients();
    }
    @CrossOrigin(origins = "*")
    @PutMapping
    public Client updateClient(@RequestBody Client client) {
        return clientService.updateClient(client);
    }
    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String deleteClient(@PathVariable Integer id) {
        clientService.deleteClient(id);
        return "Client deleted.";
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/search/nhts/{nhts}")
    public List<Client> getByNhts(@PathVariable String nhts) {
        return clientService.getClientsByNhts(nhts);
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/search/4ps/{is4PSMember}")
    public List<Client> getBy4PS(@PathVariable String is4PSMember) {
        return clientService.getClientsByIs4PSMember(is4PSMember);
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/search/civilstatus/{civilStatus}")
    public List<Client> getByCivilStatus(@PathVariable String civilStatus) {
        return clientService.getClientsByCivilStatus(civilStatus);
    }
    
}
